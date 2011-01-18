
package com.announcify.api;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.announcify.api.queue.PluginQueue;
import com.announcify.api.service.PluginService;
import com.announcify.api.util.PluginSettings;

public class AnnouncifyIntent {

    private final PluginSettings settings;

    private final Context context;

    private String startBroadcast;

    private String stopBroadcast;

    
    public AnnouncifyIntent(final Context context, final PluginSettings settings) {
        this.settings = settings;
        this.context = context;
    }
    

    public void setStartBroadcast(final String action) {
        startBroadcast = action;
    }

    public void setStopBroadcast(final String action) {
        stopBroadcast = action;
    }

    public void announce(final String announcement) {
        final LinkedList<Object> list = new LinkedList<Object>();

        if ("".equals(announcement) || announcement == null) {
            return;
        }

        for (int i = 0; i < settings.getReadingRepeat(); i++) {
            list.add(announcement);
            list.add(new Integer(settings.getReadingBreak()));
        }

        final PluginQueue queue = new PluginQueue(settings.getEventType(), list, startBroadcast,
                stopBroadcast, context);

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
}
