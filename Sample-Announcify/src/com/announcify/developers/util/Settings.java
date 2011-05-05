package com.announcify.developers.util;

import android.content.Context;

import com.announcify.api.background.util.PluginSettings;
import com.announcify.developers.sample.R;

public class Settings extends PluginSettings {

    public static final String ACTION_SETTINGS = "com.announcify.developers.SETTINGS";
    public static final String PREFERENCES_NAME = "com.announcify.developers";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        // return the event you're announcifying. must be unique!
        return context.getString(R.string.event);
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getSettingsAction() {
        return ACTION_SETTINGS;
    }

    // feel free to override default settings here, like this:
    // @Override
    // public int getReadingRepeat() {
    // return Integer.parseInt(preferences.getString(KEY_READING_REPEAT, "5"));
    // }
}
