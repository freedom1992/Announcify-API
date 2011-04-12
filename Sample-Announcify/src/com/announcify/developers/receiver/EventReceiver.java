package com.announcify.developers.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.announcify.api.background.service.PluginService;
import com.announcify.developers.service.WorkerService;

public class EventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // redirect the event to the hard-working service and let him do the
        // job.
        intent.setComponent(new ComponentName(context, WorkerService.class));
        intent.setAction(PluginService.ACTION_ANNOUNCE);
        context.startService(intent);

        // chill. :)
    }
}
