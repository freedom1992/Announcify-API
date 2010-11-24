package com.announcify.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.announcify.R;
import com.announcify.service.ManagerService;

public class RemoteControlDialog extends Activity {
	public static final String ACTION_CONTINUE = "com.announcify.ACTION_CONTINUE";
	public static final String ACTION_PAUSE = "com.announcify.ACTION_PAUSE";
	public static final String ACTION_SKIP = "com.announcify.ACTION_SKIP";

	private final String[] controls = new String[] {"Pause", "Continue", "Skip", "Kill"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Builder bob = new AlertDialog.Builder(this);
		bob.setIcon(R.drawable.icon).setTitle("Control Announcify");
		// TODO: use strings.xml array
		bob.setItems(controls, new OnClickListener() {

			public void onClick(final DialogInterface dialog, final int which) {
				switch (which) {
					case 0:
						fireBroadcast(ACTION_PAUSE);
						break;
					case 1:
						fireBroadcast(ACTION_CONTINUE);
						break;
					case 2:
						fireBroadcast(ACTION_SKIP);
						break;
					case 3:
						RemoteControlDialog.this.stopService(new Intent(RemoteControlDialog.this, ManagerService.class));
						break;
				}

				dialog.dismiss();
				finish();
			}
		});
		bob.setOnCancelListener(new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				finish();
			}
		});
		bob.create().show();
	}

	private void fireBroadcast(final String action) {
		sendBroadcast(new Intent(action));
	}
}