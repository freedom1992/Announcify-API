package com.announcify.api.background.tts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.announcify.api.background.sql.model.TranslationModel;
import com.announcify.api.background.util.AnnouncifySettings;
import com.announcify.api.background.util.Connection;

public class Translator {

    private final AnnouncifySettings settings;
    private final TranslationModel model;
    private final Context context;

    public Translator(final Context context) {
        settings = new AnnouncifySettings(context);
        model = new TranslationModel(context);
        this.context = context;
    }

    public Locale guessLanguage(final String textSnippet) {
        if (Connection.isConnected(context)) {
            try {
                final URL url = new URL("https://ajax.googleapis.com/ajax/services/language/detect?v=1.0&key=AIzaSyB1NIVM7-eAI75fOuH2RR2qDCSs59r1UhI&&q=" + URLEncoder.encode(textSnippet));
                URLConnection connection;
                connection = url.openConnection();
                connection.addRequestProperty("Referer", "http://announcify.com/");

                String line;
                final StringBuilder builder = new StringBuilder();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                final JSONObject json = new JSONObject(builder.toString());
                return new Locale((String) json.get("language"));
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }

        return Locale.getDefault();
    }

    public String translate(String text, final Locale currentLanguage, final Locale wantedLanguage) {
        if (settings.getTranslateText()) {
            text = translateOffline(text);

            text = translateOnline(text, currentLanguage, wantedLanguage);

            if (wantedLanguage != null) {
                return "<speak xml:lang=\"" + wantedLanguage.toString() + "\">" + text + "</speak>";
            } else if (currentLanguage != null) {
                return "<speak xml:lang=\"" + currentLanguage.toString() + "\">" + text + "</speak>";
            }
        } else if (settings.getGuessLanguage()) {
            return "<speak xml:lang=\"" + guessLanguage(text).toString() + "\">" + text + "</speak>";
        } else if (wantedLanguage == null) {
            return text;
        }

        return "<speak xml:lang=\"" + Locale.getDefault().toString() + "\">" + text + "</speak>";
    }

    public String translate(final String text) {
        return translate(text, null, null);
    }

    public String translateOffline(String text) {
        final Cursor cursor = model.getResolver().query(model.buildUri(), new String[] { TranslationModel.KEY_TRANSLATION_FROM, TranslationModel.KEY_TRANSLATION_TO }, null, null, null);

        final int fromIndex = cursor.getColumnIndex(TranslationModel.KEY_TRANSLATION_FROM);
        final int toIndex = cursor.getColumnIndex(TranslationModel.KEY_TRANSLATION_TO);
        while (cursor.moveToNext()) {
            text = text.replaceAll(cursor.getString(fromIndex), cursor.getString(toIndex));
        }

        return text;
    }

    public String translateOnline(final String text, Locale currentLanguage, Locale wantedLanguage) {
        if (Connection.isConnected(context)) {
            try {
                if (currentLanguage == null) {
                    String textSnippet;
                    if (text.length() > 100) {
                        final int i = text.indexOf(" ", 100);
                        textSnippet = text.substring(0, i > 0 ? i : text.length());
                    } else {
                        textSnippet = text;
                    }

                    currentLanguage = guessLanguage(textSnippet);

                    if (wantedLanguage == null || currentLanguage == null) {
                        return text;
                    }

                    if (wantedLanguage.getLanguage().equals(currentLanguage.getLanguage())) {
                        return text;
                    }
                }

                if (wantedLanguage == null) {
                    wantedLanguage = Locale.getDefault();
                }

                final URL url = new URL("https://www.googleapis.com/language/translate/v1?key=AIzaSyB1NIVM7-eAI75fOuH2RR2qDCSs59r1UhI&q=" + URLEncoder.encode(text) + "&source=" + currentLanguage.getLanguage() + "&target=" + wantedLanguage.getLanguage());
                URLConnection connection;
                connection = url.openConnection();
                connection.addRequestProperty("Referer", "http://announcify.com/");

                String line;
                final StringBuilder builder = new StringBuilder();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                final String result = new JSONObject(builder.toString()).getString("translatedText");
                final String[] words = result.split(" ");

                for (final String s : words) {
                    model.add(text, s, currentLanguage.getLanguage(), wantedLanguage.getLanguage());
                }

                return result;
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }

        return text;
    }
}
