package com.announcify.api.background.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;

public class AnnouncifySettings {

    public static final String PREFERENCES_NAME = "com.announcify.SETTINGS";

    public static final String KEY_READING_MODE = "preference_reading_mode";
    public static final String KEY_GROUP_FILTER = "preference_group_filter";
    public static final String KEY_GROUP_BLOCK = "preference_group_block";
    public static final String KEY_CONTACT_FILTER = "preference_contact_filter";
    public static final String KEY_CONTACT_BLOCK = "preference_contact_block";
    public static final String KEY_GUESS_LANGUAGE = "preference_guess_language";
    public static final String KEY_TRANSLATE_TEXT = "preference_translate_text";
    public static final String KEY_STREAM = "preference_stream";
    public static final String KEY_DISCREET = "preference_condition_discreet";
    public static final String KEY_SCREEN = "preference_condition_screen";
    public static final String KEY_GRAVITY = "preference_condition_gravity";
    public static final String KEY_NOTIFICATION = "preference_behavior_notification";
    public static final String KEY_SPECIAL_CHARACTERS = "preference_special_characters";

    private final SharedPreferences preferences;

    public AnnouncifySettings(Context context) {
        try {
            if (!"com.announcify".equals(context.getPackageName())) {
                context = context.createPackageContext("com.announcify", 0);
            }
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }

        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_WORLD_READABLE);
    }

    public boolean getBlockGroups() {
        return preferences.getBoolean(KEY_GROUP_BLOCK, false);
    }

    public String getSpecialCharacter() {
        return preferences.getString(KEY_SPECIAL_CHARACTERS, ":/-(@[{<");
    }

    public boolean getBlockContacts() {
        return preferences.getBoolean(KEY_CONTACT_BLOCK, false);
    }

    public String getDefaultDefaultMode() {
        return preferences.getString(PluginSettings.KEY_READING_ANNOUNCEMENT, "<NAME>");
    }

    public String getDefaultDiscreetMode() {
        return preferences.getString(PluginSettings.KEY_READING_DISCREET, "<NAME>");
    }

    public int getDefaultReadingBreak() {
        return Integer.parseInt(preferences.getString(PluginSettings.KEY_READING_BREAK, "2"));
    }

    public int getDefaultReadingRepeat() {
        return Integer.parseInt(preferences.getString(PluginSettings.KEY_READING_REPEAT, "1"));
    }

    public int getDefaultReadingWait() {
        return Integer.parseInt(preferences.getString(PluginSettings.KEY_READING_WAIT, "0"));
    }

    public String getDefaultUnknownMode() {
        return preferences.getString(PluginSettings.KEY_READING_UNKNOWN, "<UNKNOWN>");
    }

    public boolean getFilterByGroup() {
        return preferences.getBoolean(KEY_GROUP_FILTER, false);
    }

    public boolean getFilterByContact() {
        return preferences.getBoolean(KEY_CONTACT_FILTER, false);
    }

    public int getReadingMode() {
        return Integer.parseInt(preferences.getString(KEY_READING_MODE, "0"));
    }

    public int getStream() {
        return Integer.parseInt(preferences.getString(KEY_STREAM, "2"));
    }

    public boolean getGuessLanguage() {
        return preferences.getBoolean(KEY_GUESS_LANGUAGE, false);
    }

    public boolean getTranslateText() {
        return preferences.getBoolean(KEY_TRANSLATE_TEXT, false);
    }

    public boolean isDiscreetCondition() {
        return preferences.getBoolean(KEY_DISCREET, false);
    }

    public boolean isGravityCondition() {
        return preferences.getBoolean(KEY_GRAVITY, false);
    }

    public boolean isScreenCondition() {
        return preferences.getBoolean(KEY_SCREEN, false);
    }

    public boolean isShowNotification() {
        return preferences.getBoolean(KEY_NOTIFICATION, true);
    }

    public void setBlockGroups(final boolean enable) {
        final Editor editor = preferences.edit();
        editor.putBoolean(KEY_GROUP_BLOCK, enable);
        editor.commit();
    }

    public void setBlockContacts(final boolean enable) {
        final Editor editor = preferences.edit();
        editor.putBoolean(KEY_CONTACT_BLOCK, enable);
        editor.commit();
    }

    public String getRingtone() {
        return preferences.getString(PluginSettings.KEY_RINGTONE, "");
    }

    public String getShutUp() {
        return preferences.getString(PluginSettings.KEY_SHUT_UP, "10");
    }
}
