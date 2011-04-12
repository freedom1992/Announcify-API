package com.announcify.api;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.announcify.api.background.queue.PluginQueue;
import com.announcify.api.background.service.PluginService;
import com.announcify.api.background.util.PluginSettings;

public class AnnouncifyIntent {

    private final PluginSettings settings;
    private final Context context;
    private String startBroadcast;
    private String stopBroadcast;

    public AnnouncifyIntent(final Context context, final PluginSettings settings) {
        this.settings = settings;
        this.context = context;
    }

    public void announce(final String announcement) {
        Log.e("smn", "announce");
        final LinkedList<Object> list = new LinkedList<Object>();

        if ("".equals(announcement) || (announcement == null)) {
            return;
        }

        for (int i = 0; i < settings.getReadingRepeat(); i++) {
            list.add(announcement);
            list.add(new Integer(settings.getReadingBreak()));
        }

        final PluginQueue queue = new PluginQueue(settings.getEventType(), list, startBroadcast, stopBroadcast);

        if (queue.isEmpty()) {
            return;
        }

        if (settings.getReadingWait() > 1000) {
            try {
                Thread.sleep(settings.getReadingWait());
            } catch (final InterruptedException e) {
            }
        }

        final Intent announceIntent = new Intent(PluginService.ACTION_ANNOUNCE);
        final Bundle bundle = new Bundle();
        bundle.putInt(PluginService.EXTRA_PRIORITY, settings.getPriority());
        bundle.putParcelable(PluginService.EXTRA_QUEUE, queue);
        announceIntent.putExtras(bundle);
        context.startService(announceIntent);
    }

    public void setStartBroadcast(final String action) {
        startBroadcast = action;
    }

    public void setStopBroadcast(final String action) {
        stopBroadcast = action;
    }
}
