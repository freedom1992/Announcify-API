package com.announcify.queue;

import android.content.Context;
import android.os.PowerManager;

public class WakeLocker {
	private final PowerManager.WakeLock lock;

	public WakeLocker(final Context context) {
		final PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

		lock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "com.announcify.WAKE_LOCK");
	}

	public void lock() {
		// if service dies, wakelock will release automatically after X
		// seconds...
		lock.acquire(1000000);
	}

	public void unlock() {
		lock.release();
	}

	public boolean isLocked() {
		return lock.isHeld();
	}
}