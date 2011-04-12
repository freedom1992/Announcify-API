package com.announcify.developers.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.announcify.developers.service.WorkerService;

public class RingtoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // again, receivers are only meant to forward events to the hard-working
        // service.
        intent.setComponent(new ComponentName(context, WorkerService.class));
        context.startService(intent);
    }
}
