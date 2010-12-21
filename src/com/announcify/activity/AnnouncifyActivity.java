package com.announcify.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceActivity;

public class AnnouncifyActivity extends PreferenceActivity {
	public static String ACTION_SETTINGS;

	protected void parseRingtone(final int requestCode, final int resultCode, final Intent data, final int type) {
		if (requestCode == 100 && resultCode == RESULT_OK) {
			final Editor editor = getPreferenceManager().getSharedPreferences().edit();

			if (data != null && data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null) {
				editor.putString("preference_ringtone", data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString());

				RingtoneManager.setActualDefaultRingtoneUri(this, type, null);
			} else {
				editor.putString("preference_ringtone", "");

				final Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, type);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Choose new Ringtone to be played by Android");

				startActivityForResult(intent, 101);
			}

			editor.commit();
		} else if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null) {
			RingtoneManager.setActualDefaultRingtoneUri(this, type, (Uri) data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
		}
	}
}
