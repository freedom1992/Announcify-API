package com.announcify.util;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class PluginSettings {
	protected static final String KEY_READING_BREAK = "preference_reading_break";
	protected static final String KEY_READING_REPEAT = "preference_reading_repeat";
	protected static final String KEY_READING_WAIT = "preference_reading_wait";

	protected static final String KEY_READING_UNKNOWN = "preference_reading_unknown";
	protected static final String KEY_READING_DISCREET = "preference_reading_discreet";
	protected static final String KEY_READING_ANNOUNCEMENT = "preference_reading_announcement";

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

	public int getUnknownMode() {
		return Integer.parseInt(preferences.getString(KEY_READING_UNKNOWN, "0"));
	}

	public int getAnnouncementMode() {
		return Integer.parseInt(preferences.getString(KEY_READING_ANNOUNCEMENT, "0"));
	}

	public int getDiscreetMode() {
		return Integer.parseInt(preferences.getString(KEY_READING_DISCREET, "0"));
	}

	public String getSharedPreferencesName() {
		return PREFERENCES_NAME;
	}

	public String getCustomAnnouncement() {
		// TODO implement custom announcement
		return "";
	}

	public abstract String getEventType();
}
