package com.announcify.service;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;

import com.announcify.activity.RemoteControlDialog;
import com.announcify.queue.Queue;
import com.announcify.tts.Speaker;

public class ManagerService extends Service {
	private static Boolean paid;

	public static boolean isPaid(final Context context) {
		// TODO: this would not work if user runs the app, donates and runs the
		// app again!
		if (paid != null) {
			return paid;
		}
		try {
			// TODO: change package?
			context.createPackageContext("com.announcify.paid", 0);

			return true;
		} catch (final Exception e) {
			// bad boy... you didn't donate!
			return false;
		}
	}

	private Queue queue;
	private Speaker speaker;

	private ScreenReceiver screenReceiver;
	private CallReceiver callReceiver;
	private ControlReceiver controlReceiver;

	@Override
	public void onCreate() {
		screenReceiver = new ScreenReceiver();
		final IntentFilter screenFilter = new IntentFilter();
		screenFilter.addAction(Intent.ACTION_SCREEN_ON);
		screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenReceiver, screenFilter);

		callReceiver = new CallReceiver();
		// not sure if this works...
		registerReceiver(callReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
		// if not, use this
		// TelephonyManager telephonyManager = (TelephonyManager)
		// getSystemService(TELEPHONY_SERVICE);
		// telephonyManager.listen(listener, TelephonyManager.)

		if (isPaid(this)) {
			controlReceiver = new ControlReceiver();
			final IntentFilter controlFilter = new IntentFilter();
			controlFilter.addAction(RemoteControlDialog.ACTION_CONTINUE);
			controlFilter.addAction(RemoteControlDialog.ACTION_PAUSE);
			controlFilter.addAction(RemoteControlDialog.ACTION_SKIP);
			registerReceiver(controlReceiver, controlFilter);
		}

		// TODO: run initialization in seperate thread to fill the queue
		// parallely?
		speaker = new Speaker(this, new TextToSpeech.OnInitListener() {

			public void onInit(final int status) {
				if (status == TextToSpeech.SUCCESS) {
					queue.grant();
					speaker.setOnUtteranceCompletedListener(queue);
				} else {
					// TODO: send log to server
				}
			}
		});

		queue = new Queue(this, speaker);
	}

	@Override
	public void onStart(final Intent intent, final int startId) {
		// TODO: add to queue
	}

	@Override
	public void onDestroy() {
		if (screenReceiver != null) {
			unregisterReceiver(screenReceiver);
		}
		if (callReceiver != null) {
			unregisterReceiver(callReceiver);
		}
		if (controlReceiver != null) {
			unregisterReceiver(controlReceiver);
		}
	}

	@Override
	public IBinder onBind(final Intent arg0) {
		return null;
	}

	private class ScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				queue.deny();

				// shut down if user didn't pay, because he's not able to
				// continue queue.
				if (isPaid(ManagerService.this)) {
					stopSelf();
				}
			}
		}
	}

	private class CallReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
				queue.deny();

				// shut down if user didn't pay, because he's not able to
				// continue queue.
				if (isPaid(ManagerService.this)) {
					stopSelf();
				}
			}
		}
	}

	private class ControlReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (RemoteControlDialog.ACTION_CONTINUE.equals(intent.getAction())) {
				queue.grant();
			} else if (RemoteControlDialog.ACTION_SKIP.equals(intent.getAction())) {
				queue.deny();
				queue.grant();
			} else if (RemoteControlDialog.ACTION_PAUSE.equals(intent.getAction())) {
				queue.deny();
			}
		}
	}
}