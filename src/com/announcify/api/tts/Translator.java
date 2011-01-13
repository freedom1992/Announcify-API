
package com.announcify.api.tts;

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

import com.announcify.api.sql.model.TranslationModel;

public class Translator {
    private final TranslationModel model;

    public Translator(final Context context) {
        model = new TranslationModel(context);
    }

    public Locale guessLanguage(final String textSnippet) {
        try {
            final URL url = new URL(
                    "https://ajax.googleapis.com/ajax/services/language/detect?v=1.0&key=AIzaSyB1NIVM7-eAI75fOuH2RR2qDCSs59r1UhI&&q="
                            + URLEncoder.encode(textSnippet));
            URLConnection connection;
            connection = url.openConnection();
            connection.addRequestProperty("Referer", "http://announcify.com/");

            String line;
            final StringBuilder builder = new StringBuilder();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            final JSONObject json = new JSONObject(builder.toString());
            return new Locale((String)json.get("language"));
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        return Locale.getDefault();
    }

    public String translate(final String text, Locale currentLanguage, Locale wantedLanguage) {
        final Cursor cursor = model.getDatabase()
                .query(TranslationModel.TABLE_NAME, null,
                        TranslationModel.KEY_PLUGIN_FROM + " = " + "'" + text + "'", null, null,
                        null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(TranslationModel.KEY_PLUGIN_TO));
        }

        try {
            if (currentLanguage == null) {
                String textSnippet;
                if (text.length() > 20) {
                    final int i = text.indexOf(" ", 20);
                    textSnippet = text.substring(0, i > 0 ? i : text.length());
                } else {
                    textSnippet = text;
                }

                currentLanguage = guessLanguage(textSnippet);
                if (wantedLanguage.getLanguage().equals(currentLanguage.getLanguage())) {
                    return text;
                }
            }

            if (wantedLanguage == null) {
                wantedLanguage = Locale.getDefault();
            }

            final URL url = new URL(
                    "https://www.googleapis.com/language/translate/v1?key=AIzaSyB1NIVM7-eAI75fOuH2RR2qDCSs59r1UhI&q="
                            + URLEncoder.encode(text) + "&source=" + currentLanguage.getLanguage()
                            + "&target=" + wantedLanguage.getLanguage());
            URLConnection connection;
            connection = url.openConnection();
            connection.addRequestProperty("Referer", "http://announcify.com/");

            String line;
            final StringBuilder builder = new StringBuilder();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            final String result = new JSONObject(builder.toString()).getString("translatedText");
            model.add(text, result, currentLanguage.getLanguage(), wantedLanguage.getLanguage());
            return result;
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            model.close();
        }

        return text;
    }
}
