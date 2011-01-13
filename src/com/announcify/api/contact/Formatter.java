
package com.announcify.api.contact;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.announcify.api.util.AnnouncifySettings;
import com.announcify.api.util.HeadsetFinder;
import com.announcify.api.util.PluginSettings;

public class Formatter {

    // TODO: move to FormatEnum-Interface!
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

    private Map<String, ContactEnum> substitutes;

    private Context context;

    private Contact contact;

    private String text;

    public Formatter(final Context context, final Contact contact, final PluginSettings settings) {
        this(new HashMap<String, ContactEnum>(), context, contact, settings);

        for (final ContactEnum value : ContactEnum.values()) {
            addSubstitute(value);
        }
    }

    public Formatter(final Map<String, ContactEnum> substitutes, final Context context,
            final Contact contact, final PluginSettings settings) {
        this.substitutes = substitutes;
        this.context = context;
        this.contact = contact;

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

    public String format(final String message) {
        // TODO: ugh!
        text = text.replaceAll(NAME, getUserPreferredReadingMode());

        if (message != null) {
            text = text.replaceAll(MESSAGE, message);
        }

        for (final String s : substitutes.keySet()) {
            // TODO: i don't want to pass context and contact here everytime! ->
            // ContactEnum in Contact?
            text = text.replaceAll(s, substitutes.get(s).getSubstitution(context, contact));
        }

        return text;
    }

    public String getUserPreferredReadingMode() {
        switch (new AnnouncifySettings(context).getReadingMode()) {
            case 0: // user requested to read full name
                return FULLNAME;

            case 1: // user requested to read only first name
                return FIRSTNAME;

            case 2: // user requested to read only family name
                return LASTNAME;

            case 3: // user requested to read nickname (if available)
                return NICKNAME;

            case 4: // user requested to read the number
                return ADDRESS;

            default:
                return "";
        }
    }
}
