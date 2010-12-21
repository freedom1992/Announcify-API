package com.announcify.contact;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class Filter {
	public static final String PREFERENCE_GROUP = "com.announcify.PREFERENCE_GROUP";

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @return Returns true if this contact isn't in user-desired group.
	 */
	public static boolean checkGroup(final Context context, final int groupId) {
		try {
			if (context.createPackageContext("com.announcify", 0).getSharedPreferences("org.mailboxer.saymyname.group", Context.MODE_WORLD_READABLE).getAll().containsValue(groupId)) {
				return true;
			} else {
				return false;
			}
		} catch (final NameNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
}