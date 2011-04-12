package com.announcify.api.ui.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.announcify.api.R;
import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.util.PluginSettings;
import com.markupartist.android.widget.ActionBar;

public class PluginActivity extends PreferenceActivity {

    protected static final String CUSTOM_VALUE = "com.announcify.CUSTOM";

    protected PluginSettings settings;

    protected void applyThemeProtection(final String key) {
        getPreferenceScreen().findPreference(key).setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(final Preference preference) {
                ((PreferenceScreen) preference).getDialog().getWindow().setTitle("");
                ((PreferenceScreen) preference).getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                return false;
            }
        });
    }

    protected void onCreate(final Bundle savedInstanceState, final PluginSettings settings, final int xml) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setTheme(android.R.style.Theme_Light_NoTitleBar);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.actionbar_list);

        getListView().setBackgroundColor(Color.WHITE);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setFastScrollEnabled(true);

        this.settings = settings;

        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(settings.getEventType());

        actionBar.setHomeAction(new ActionBar.IntentAction(this, ActivityUtils.getHomeIntent(), R.drawable.launcher_icon));

        actionBar.addAction(new ActionBar.IntentAction(this, ActivityUtils.getHelpIntent(), R.drawable.gd_action_bar_talk_normal));
        actionBar.addAction(new ActionBar.IntentAction(this, ActivityUtils.getPluginsIntent(), R.drawable.gd_action_bar_add_normal));
        actionBar.addAction(new ActionBar.IntentAction(this, ActivityUtils.getShareIntent(this), R.drawable.gd_action_bar_share_normal));

        getPreferenceManager().setSharedPreferencesName(settings.getSharedPreferencesName());
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);

        addPreferencesFromResource(xml);

        setCustomListeners();
    }

    protected void parseRingtone(final int requestCode, final int resultCode, final Intent data, final int type) {
        if ((requestCode == 100) && (resultCode == RESULT_OK)) {
            final Editor editor = getPreferenceManager().getSharedPreferences().edit();

            if ((data != null) && (data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null)) {
                editor.putString(PluginSettings.KEY_RINGTONE, data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString());

                RingtoneManager.setActualDefaultRingtoneUri(this, type, null);
            } else {
                editor.putString(PluginSettings.KEY_RINGTONE, "");

                final Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, type);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.preference_ringtone_system));

                startActivityForResult(intent, 101);
            }

            editor.commit();
        } else if ((requestCode == 101) && (resultCode == RESULT_OK) && (data != null) && (data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) != null)) {
            RingtoneManager.setActualDefaultRingtoneUri(this, type, (Uri) data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
        }
    }

    protected void setCustomAnnouncementListener(final String key, final PluginSettings settings, final String value) {
        final String oldValue = getPreferenceManager().getSharedPreferences().getString(key, "");

        getPreferenceScreen().findPreference(key).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                if (!((String) newValue).equals(value)) {
                    return true;
                }

                final LinearLayout layout = new LinearLayout(PluginActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText edit = new EditText(PluginActivity.this);
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
                edit.setHint(getString(R.string.preference_formatstring_input_hint));
                edit.setText(getPreferenceManager().getSharedPreferences().getString(preference.getKey(), ""));

                final Button button = new Button(PluginActivity.this);
                button.setText(getString(R.string.preference_formatstring_add_variable));
                button.setOnClickListener(new View.OnClickListener() {

                    public void onClick(final View v) {
                        final String[] items = getResources().getStringArray(R.array.dialog_list_formatstring_variables);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(PluginActivity.this);
                        builder.setTitle(getString(R.string.preference_formatstring_add_variable));
                        builder.setItems(items, new DialogInterface.OnClickListener() {

                            public void onClick(final DialogInterface dialog, final int item) {
                                edit.append(items[item]);

                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                });

                layout.addView(edit);
                layout.addView(button);

                final Builder builder = new AlertDialog.Builder(PluginActivity.this);
                builder.setTitle(getString(R.string.preference_custom_text_title));
                builder.setView(layout);
                builder.setPositiveButton(getString(R.string.preference_custom_save), new OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int which) {
                        final Editor editor = getPreferenceManager().getSharedPreferences().edit();
                        editor.putString(key, edit.getText().toString());
                        editor.commit();
                    }
                });
                builder.setCancelable(false);
                builder.setNegativeButton(getString(R.string.preference_custom_cancel), new OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int which) {
                        final Editor editor = getPreferenceManager().getSharedPreferences().edit();
                        editor.putString(key, oldValue);
                        editor.commit();
                    }
                });
                builder.create().show();

                return true;
            }
        });
    }

    protected void setCustomListeners() {
        setCustomNumberListener(PluginSettings.KEY_READING_WAIT, settings, CUSTOM_VALUE);
        setCustomNumberListener(PluginSettings.KEY_READING_BREAK, settings, CUSTOM_VALUE);
        setCustomNumberListener(PluginSettings.KEY_READING_REPEAT, settings, CUSTOM_VALUE);

        setCustomAnnouncementListener(PluginSettings.KEY_READING_ANNOUNCEMENT, settings, CUSTOM_VALUE);
        setCustomAnnouncementListener(PluginSettings.KEY_READING_DISCREET, settings, CUSTOM_VALUE);
        setCustomAnnouncementListener(PluginSettings.KEY_READING_UNKNOWN, settings, CUSTOM_VALUE);
    }

    protected void setExtraCustomListeners() {
        setCustomNumberListener(PluginSettings.KEY_SHUT_UP, settings, CUSTOM_VALUE);
    }

    protected void setCustomNumberListener(final String key, final PluginSettings settings, final String customValue) {
        final String oldValue = getPreferenceManager().getSharedPreferences().getString(key, "");

        getPreferenceScreen().findPreference(key).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                if (!((String) newValue).equals(customValue)) {
                    return true;
                }

                final EditText edit = new EditText(PluginActivity.this);
                edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                edit.setHint(getString(R.string.preference_custom_value_input_hint));

                final Builder builder = new AlertDialog.Builder(PluginActivity.this);
                builder.setTitle(getString(R.string.preference_custom_value_title));
                builder.setView(edit);
                builder.setPositiveButton(getString(R.string.preference_custom_save), new OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int which) {
                        final Editor editor = getPreferenceManager().getSharedPreferences().edit();
                        editor.putString(key, edit.getText().toString());
                        editor.commit();
                    }
                });
                builder.setCancelable(false);
                builder.setNegativeButton(getString(R.string.preference_custom_cancel), new OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int which) {
                        final Editor editor = getPreferenceManager().getSharedPreferences().edit();
                        editor.putString(key, oldValue);
                        editor.commit();
                    }
                });
                builder.create().show();

                return true;
            }
        });
    }
}