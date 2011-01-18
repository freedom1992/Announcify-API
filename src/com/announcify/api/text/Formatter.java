
package com.announcify.api.text;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.announcify.api.audio.HeadsetFinder;
import com.announcify.api.contact.Contact;
import com.announcify.api.contact.ContactEnum;
import com.announcify.api.util.AnnouncifySettings;
import com.announcify.api.util.PluginSettings;

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

    
    private Map<String, ContactEnum> substitutes;

    private PluginSettings settings;

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
        this.settings = settings;
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
        Log.e("smn", text);
        text = text.replaceAll(NAME, getUserPreferredReadingMode());
        Log.e("smn", text);

        if (message != null) {
            text = text.replaceAll(MESSAGE, message);
        }
        Log.e("smn", text);

        text = text.replaceAll(EVENT, settings.getEventType());
        Log.e("smn", text);

        for (final String s : substitutes.keySet()) {
            // TODO: i don't want to pass context and contact here everytime! ->
            // make ContactEnum inner class of Contact?
            text = text.replaceAll(s, substitutes.get(s).getSubstitution(context, contact));
            Log.e("smn", text);
        }

        return text;
    }

    public String getUserPreferredReadingMode() {
        switch (new AnnouncifySettings(context).getReadingMode()) {
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
}
