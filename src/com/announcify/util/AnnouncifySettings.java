package com.announcify.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class AnnouncifySettings {
	public static final String PREFERENCES_NAME = "com.announcify.SETTINGS";

	private final SharedPreferences preferences;

	public static final String KEY_READING_MODE = "preference_reading_mode";

	public static final String KEY_FILTER_BY_GROUP = "preference_filter_by_group";

	public static final String KEY_STREAM = "preference_stream";

	public static final String KEY_DISCREET = "preference_condition_discreet";
	public static final String KEY_SCREEN = "preference_condition_screen";
	public static final String KEY_GRAVITY = "preference_condition_gravity";

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

	public int getReadingMode() {
		return Integer.parseInt(preferences.getString(KEY_READING_MODE, "0"));
	}

	public boolean getFilterByGroup() {
		return preferences.getBoolean(KEY_FILTER_BY_GROUP, false);
	}

	public int getStream() {
		return Integer.parseInt(preferences.getString(KEY_STREAM, "2"));
	}

	public boolean isScreenCondition() {
		return preferences.getBoolean(KEY_SCREEN, true);
	}

	public boolean isDiscreetCondition() {
		return preferences.getBoolean(KEY_DISCREET, false);
	}

	public boolean isGravityCondition() {
		return preferences.getBoolean(KEY_GRAVITY, true);
	}
}