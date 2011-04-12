package com.announcify.developers.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.announcify.api.ui.activity.PluginActivity;
import com.announcify.developers.sample.R;
import com.announcify.developers.util.Settings;

public class SettingsActivity extends PluginActivity {

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // the user chose a ringtone. let's try to silent the system's
        // notification sound.
        parseRingtone(requestCode, resultCode, data, RingtoneManager.TYPE_NOTIFICATION);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        // fill the activity with basic settings
        super.onCreate(savedInstanceState, new Settings(this), R.xml.preferences_settings);

        // if you need some more advanced settings, do this:
        // addPreferencesFromResource(R.xml.preferences_extra_settings);
        // setExtraCustomListeners();
    }
}
