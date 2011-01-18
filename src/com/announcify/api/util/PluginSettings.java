
package com.announcify.api.util;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class PluginSettings {

    public static final String KEY_RINGTONE = "preference_ringtone";

    public static final String KEY_READING_BREAK = "preference_reading_break";

    public static final String KEY_READING_REPEAT = "preference_reading_repeat";

    public static final String KEY_READING_WAIT = "preference_reading_wait";

    public static final String KEY_READING_UNKNOWN = "preference_reading_unknown";

    public static final String KEY_READING_UNKNOWN_CUSTOM = "preference_reading_unknown_custom";

    public static final String KEY_READING_DISCREET = "preference_reading_discreet";

    public static final String KEY_READING_DISCREET_CUSTOM = "preference_reading_discreet_custom";

    public static final String KEY_READING_ANNOUNCEMENT = "preference_reading_announcement";

    public static final String KEY_READING_ANNOUNCEMENT_CUSTOM = "preference_reading_announcement_custom";

    protected final String PREFERENCES_NAME;

    protected final SharedPreferences preferences;

    public PluginSettings(final Context context, final String preferencesName) {
        PREFERENCES_NAME = preferencesName;
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_WORLD_READABLE);
    }

    public int getReadingBreak() {
        final int i = Integer.parseInt(preferences.getString(KEY_READING_BREAK, "2"));

        if (i == 0) {
            return 0;
        }

        return i < 1000 ? i * 1000 : i;
    }

    public int getReadingRepeat() {
        return Integer.parseInt(preferences.getString(KEY_READING_REPEAT, "1"));
    }

    public int getReadingWait() {
        final int i = Integer.parseInt(preferences.getString(KEY_READING_WAIT, "0"));

        if (i == 0) {
            return 0;
        }

        return i < 1000 ? i * 1000 : i;
    }

    public String getUnknownMode() {
        return preferences.getString(KEY_READING_UNKNOWN, "");
    }

    public String getDefaultMode() {
        return preferences.getString(KEY_READING_ANNOUNCEMENT, "<NAME>");
    }

    public String getDiscreetMode() {
        return preferences.getString(KEY_READING_DISCREET, "<NAME>");
    }

    public String getSharedPreferencesName() {
        return PREFERENCES_NAME;
    }

    public abstract String getEventType();

    public abstract int getPriority();
}
