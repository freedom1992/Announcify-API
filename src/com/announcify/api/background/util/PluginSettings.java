package com.announcify.api.background.util;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class PluginSettings {

    public static final String DEFAULT_SETTING = "com.announcify.DEFAULT";

    public static final String KEY_RINGTONE = "preference_ringtone";
    public static final String KEY_READING_BREAK = "preference_reading_break";
    public static final String KEY_READING_REPEAT = "preference_reading_repeat";
    public static final String KEY_READING_WAIT = "preference_reading_wait";
    public static final String KEY_READING_UNKNOWN = "preference_reading_unknown";
    public static final String KEY_READING_DISCREET = "preference_reading_discreet";
    public static final String KEY_READING_ANNOUNCEMENT = "preference_reading_announcement";

    public static final String KEY_READ_OWN = "preference_read_own";
    public static final String KEY_SHUT_UP = "preference_shut_up";
    public static final String KEY_MESSAGE = "preference_read_message";
    public static final String KEY_CHUCK_NORRIS = "preference_chuck_norris";
    public static final String KEY_ANNOYING_MODE = "preference_annoying_mode";

    protected final String name;
    protected final Context context;
    protected final SharedPreferences preferences;
    protected final AnnouncifySettings defaultSettings;

    public PluginSettings(final Context context, final String name) {
        preferences = context.getSharedPreferences(name, Context.MODE_WORLD_READABLE);
        defaultSettings = new AnnouncifySettings(context);
        this.context = context;
        this.name = name;
    }

    public String getDefaultMode() {
        final String s = preferences.getString(KEY_READING_ANNOUNCEMENT, DEFAULT_SETTING);
        if (s.equals(DEFAULT_SETTING)) {
            return defaultSettings.getDefaultDefaultMode();
        } else {
            return s;
        }
    }

    public String getDiscreetMode() {
        final String s = preferences.getString(KEY_READING_DISCREET, defaultSettings.getDefaultDiscreetMode());
        if (s.equals(DEFAULT_SETTING)) {
            return defaultSettings.getDefaultDiscreetMode();
        } else {
            return s;
        }
    }

    public abstract String getEventType();

    public abstract int getPriority();

    public int getReadingBreak() {
        int i;

        final String s = preferences.getString(KEY_READING_BREAK, String.valueOf(defaultSettings.getDefaultReadingBreak()));
        if (s.equals(DEFAULT_SETTING)) {
            i = defaultSettings.getDefaultReadingBreak();
        } else {
            i = Integer.parseInt(s);
        }

        return i < 1000 ? i * 1000 : i;
    }

    public int getReadingRepeat() {
        final String s = preferences.getString(KEY_READING_REPEAT, String.valueOf(defaultSettings.getDefaultReadingRepeat()));
        if (s.equals(DEFAULT_SETTING)) {
            return defaultSettings.getDefaultReadingRepeat();
        } else {
            return Integer.parseInt(s);
        }
    }

    public int getReadingWait() {
        int i;

        final String s = preferences.getString(KEY_READING_WAIT, String.valueOf(defaultSettings.getDefaultReadingWait()));
        if (s.equals(DEFAULT_SETTING)) {
            i = defaultSettings.getDefaultReadingWait();
        } else {
            i = Integer.parseInt(s);
        }

        return i < 1000 ? i * 1000 : i;
    }

    public abstract String getSettingsAction();

    public String getSharedPreferencesName() {
        return name;
    }

    public String getUnknownMode() {
        final String s = preferences.getString(KEY_READING_UNKNOWN, defaultSettings.getDefaultUnknownMode());
        if (s.equals(DEFAULT_SETTING)) {
            return defaultSettings.getDefaultUnknownMode();
        } else {
            return s;
        }
    }

    public String getRingtone() {
        return preferences.getString(KEY_RINGTONE, defaultSettings.getRingtone());
    }

    public int getReadMessageMode() {
        return Integer.parseInt(preferences.getString(KEY_MESSAGE, "0"));
    }

    public boolean getReadOwn() {
        return preferences.getBoolean(KEY_READ_OWN, false);
    }

    public int getShutUp() {
        final String s = preferences.getString(KEY_SHUT_UP, defaultSettings.getShutUp());
        if (s.equals(DEFAULT_SETTING)) {
            return Integer.parseInt(defaultSettings.getShutUp()) * 1000;
        } else {
            return Integer.parseInt(s) * 1000;
        }
    }

    public boolean isChuckNorris() {
        return preferences.getBoolean(KEY_CHUCK_NORRIS, false);
    }

    public boolean isAnnoyingMode() {
        return preferences.getBoolean(KEY_ANNOYING_MODE, true);
    }
}
