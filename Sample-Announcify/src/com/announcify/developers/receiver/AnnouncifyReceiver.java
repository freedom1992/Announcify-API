package com.announcify.developers.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.announcify.developers.service.WorkerService;

public class AnnouncifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // the user started Announcify. now we need to register our plugin in
        // its database in order to show up properly.
        intent.setComponent(new ComponentName(context, WorkerService.class));
        context.startService(intent);
    }
}
