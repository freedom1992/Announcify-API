package com.announcify.api.background.text;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.announcify.api.background.audio.HeadsetFinder;
import com.announcify.api.background.contact.Contact;
import com.announcify.api.background.contact.ContactEnum;
import com.announcify.api.background.tts.Translator;
import com.announcify.api.background.util.AnnouncifySettings;
import com.announcify.api.background.util.PluginSettings;

public class Formatter {

    public static final String EVENT = "<EVENT>";
    public static final String NAME = "<NAME>"; // as set per full-settings!
    public static final String UNKNOWN = "<UNKNOWN>";
    public static final String FULLNAME = "<FULLNAME>";
    public static final String FIRSTNAME = "<FIRSTNAME>";
    public static final String LASTNAME = "<LASTNAME>";
    public static final String NICKNAME = "<NICKNAME>";
    public static final String TITLE = "<TITLE>";
    public static final String ADDRESS = "<ADDRESS>";
    public static final String ADDRESS_TYPE = "<ADDRESSTYPE>";
    public static final String MESSAGE = "<MESSAGE>";

    private final AnnouncifySettings announcifySettings;
    private final Map<String, ContactEnum> substitutes;
    private final PluginSettings settings;
    private final Context context;
    private final Contact contact;
    private String text;

    public Formatter(final Context context, final Contact contact, final PluginSettings settings) {
        this(new HashMap<String, ContactEnum>(), context, contact, settings);

        for (final ContactEnum value : ContactEnum.values()) {
            addSubstitute(value);
        }
    }

    public Formatter(final Map<String, ContactEnum> substitutes, final Context context, final Contact contact, final PluginSettings settings) {
        this.substitutes = substitutes;
        this.settings = settings;
        this.context = context;
        this.contact = contact;
        announcifySettings = new AnnouncifySettings(context);

        if ("".equals(contact.getLookupString())) {
            // seems like we couldn't find the contact in the database! so, i
            // assume he's unknown!
            text = settings.getUnknownMode();

            if ("".equals(text)) {
                return;
            }
        } else {
            if (HeadsetFinder.isDiscreet(context)) {
                text = settings.getDiscreetMode();
            } else {
                // TODO: if (screen) ...
                // TODO: if (gravitiy) ...
                // ...
                text = settings.getDefaultMode();
            }
        }
    }

    public void addSubstitute(final ContactEnum substitute) {
        substitutes.put(substitute.getExpression(), substitute);
    }

    public String format(String message) {
        final String preferredName = substitutes.get(getUserPreferredReadingMode()).getSubstitution(context, contact);
        text = text.replaceAll(NAME, cutText(preferredName));

        if (message == null) {
            message = "";
        }

        message = message.replaceAll(AndroidPatterns.WEB_URL.pattern(), "URL");
        message = cutText(message);
        message = translate(message);
        text = text.replaceAll(MESSAGE, message);

        text = text.replaceAll(EVENT, settings.getEventType());

        for (final String s : substitutes.keySet()) {
            String temp = substitutes.get(s).getSubstitution(context, contact);
            if (temp == null) {
                temp = "";
            }

            // TODO: i don't want to pass context and contact here everytime! ->
            // make ContactEnum inner class of Contact?
            text = text.replaceAll(s, substitutes.get(s).getSubstitution(context, contact));
        }

        text = translate(text);

        return text;
    }

    private String translate(final String translate) {
        final Translator translator = new Translator(context);
        return translator.translate(translate);
    }

    public String getUserPreferredReadingMode() {
        switch (announcifySettings.getReadingMode()) {
            case 0:
                return FULLNAME;

            case 1:
                return FIRSTNAME;

            case 2:
                return LASTNAME;

            case 3:
                return NICKNAME;

            case 4:
                return ADDRESS;

            default:
                return "";
        }
    }

    public String cutText(final String cutMe) {
        if (cutMe == null) {
            return "";
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final String specials = announcifySettings.getSpecialCharacter();

        for (int i = 0; i < cutMe.length(); i++) {
            for (int j = 0; j < specials.length(); j++) {
                if (cutMe.charAt(i) == specials.charAt(j)) {
                    return stringBuilder.toString();
                }
            }

            stringBuilder.append(cutMe.charAt(i));
        }

        return cutMe;
    }
}
