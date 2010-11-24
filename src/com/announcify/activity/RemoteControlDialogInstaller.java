package com.announcify.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.announcify.R;

public class RemoteControlDialogInstaller extends Activity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent shortcutIntent = new Intent(this, RemoteControlDialog.class);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		final Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Announcify RemoteControl");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));

		setResult(RESULT_OK, intent);
		finish();
	}
}