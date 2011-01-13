
package com.announcify.api.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.text.InputType;
import android.widget.EditText;

import com.announcify.api.util.PluginSettings;

public class PluginActivity extends PreferenceActivity {
    public static String ACTION_SETTINGS;

    protected void setCustomListeners(final PluginSettings settings) {
        setCustomNumberListener(PluginSettings.KEY_READING_WAIT, settings, "7");
        setCustomNumberListener(PluginSettings.KEY_READING_BREAK, settings, "4");
        setCustomNumberListener(PluginSettings.KEY_READING_REPEAT, settings, "7");

        setCustomAnnouncementListener(PluginSettings.KEY_READING_ANNOUNCEMENT, settings,
                "com.announcify.CUSTOM");
        setCustomAnnouncementListener(PluginSettings.KEY_READING_DISCREET, settings,
                "com.announcify.CUSTOM");
        setCustomAnnouncementListener(PluginSettings.KEY_READING_UNKNOWN, settings,
                "com.announcify.CUSTOM");
    }

    private void setCustomNumberListener(final String key, final PluginSettings settings,
            final String value) {
        getPreferenceScreen().findPreference(key).setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(final Preference preference,
                            final Object newValue) {
                        if (((String)newValue).equals(value)) {
                            return true;
                        }

                        final EditText edit = new EditText(PluginActivity.this);
                        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edit.setHint("Something between 0 and infinity...");
                        final Builder builder = new AlertDialog.Builder(PluginActivity.this);
                        builder.setTitle("Choose a custom value");
                        builder.setView(edit);
                        builder.setPositiveButton("Save", new OnClickListener() {

                            public void onClick(final DialogInterface dialog, final int which) {
                                final Editor editor = PluginActivity.this.getSharedPreferences(
                                        settings.getSharedPreferencesName(),
                                        Context.MODE_WORLD_READABLE).edit();
                                editor.putString(key, edit.getText().toString());
                                editor.commit();
                            }
                        });
                        builder.setCancelable(false);
                        builder.setNegativeButton("Cancel", new OnClickListener() {

                            public void onClick(final DialogInterface dialog, final int which) {
                            }
                        });
                        builder.create().show();

                        return true;
                    }
                });
    }

    private void setCustomAnnouncementListener(final String key, final PluginSettings settings,
            final String value) {
        getPreferenceScreen().findPreference(key).setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(final Preference preference,
                            final Object newValue) {
                        if (((String)newValue).equals(value)) {
                            return true;
                        }

                        final EditText edit = new EditText(PluginActivity.this);
                        edit.setInputType(InputType.TYPE_CLASS_TEXT);
                        edit.setHint("& for name, and % for adress type");
                        final Builder builder = new AlertDialog.Builder(PluginActivity.this);
                        builder.setTitle("Choose a custom text");
                        builder.setView(edit);
                        builder.setPositiveButton("Save", new OnClickListener() {

                            public void onClick(final DialogInterface dialog, final int which) {
                                final Editor editor = PluginActivity.this.getSharedPreferences(
                                        settings.getSharedPreferencesName(),
                                        Context.MODE_WORLD_READABLE).edit();
                                editor.putString(key, edit.getText().toString());
                                editor.commit();
                            }
                        });
                        builder.setCancelable(false);
                        builder.setNegativeButton("Cancel", new OnClickListener() {

                            public void onClick(final DialogInterface dialog, final int which) {
                                final Editor editor = PluginActivity.this.getSharedPreferences(
                                        settings.getSharedPreferencesName(),
                                        Context.MODE_WORLD_READABLE).edit();
                                editor.putString(key, "$2");
                                editor.commit();
                            }
                        });
                        builder.create().show();

                        return true;
                    }
                });
    }

    protected void parseRingtone(final int requestCode, final int resultCode, final Intent data,
            final int type) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            final Editor editor = getPreferenceManager().getSharedPreferences().edit();

            if (data != null
                    && data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null) {
                editor.putString("preference_ringtone",
                        data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                                .toString());

                RingtoneManager.setActualDefaultRingtoneUri(this, type, null);
            } else {
                editor.putString("preference_ringtone", "");

                final Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, type);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                        "Choose new Ringtone to be played by Android");

                startActivityForResult(intent, 101);
            }

            editor.commit();
        } else if (requestCode == 101 && resultCode == RESULT_OK && data != null
                && data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null) {
            RingtoneManager.setActualDefaultRingtoneUri(this, type,
                    (Uri)data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
        }
    }
}
