
package com.announcify.api.queue;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.announcify.api.service.PluginService;
import com.announcify.api.tts.Speech;
import com.announcify.api.util.PluginSettings;

public class PluginQueue implements Parcelable {
    // this MUST be a public static final variable!!
    public static final Creator<PluginQueue> CREATOR = new Creator<PluginQueue>() {
        public PluginQueue createFromParcel(final Parcel source) {
            // TODO: called twice
            return new PluginQueue(source);
        }

        public PluginQueue[] newArray(final int size) {
            return new PluginQueue[size];
        }
    };

    public static LinkedList<Object> buildList(final PluginSettings settings, final String text) {
        final LinkedList<Object> list = new LinkedList<Object>();

        if ("".equals(text) || text == null) {
            return list;
        }

        for (int i = 0; i < settings.getReadingRepeat(); i++) {
            list.add(text);
            list.add(new Integer(settings.getReadingBreak()));
        }

        return list;
    }

    private final LinkedList<Object> queue;

    private final String pluginName;

    private String startBroadcast;

    private String stopBroadcast;

    private Speech speech;

    public PluginQueue(final String pluginName, final LinkedList<Object> list, final Context context) {
        this.pluginName = pluginName;
        queue = list;
        startBroadcast = "";
        stopBroadcast = "";
        speech = new Speech(context);
    }

    public PluginQueue(final String pluginName, final LinkedList<Object> list,
            final String startBroadcast, final String stopBroadcast, final Context context) {
        this.pluginName = pluginName;
        queue = list;
        this.startBroadcast = startBroadcast;
        this.stopBroadcast = stopBroadcast;
        speech = new Speech(context);
    }

    public PluginQueue(final String pluginName, final LinkedList<Object> list,
            final String startBroadcast, final String stopBroadcast, final Speech speech) {
        this.pluginName = pluginName;
        queue = list;
        this.startBroadcast = startBroadcast;
        this.stopBroadcast = stopBroadcast;
        this.speech = speech;
    }

    public PluginQueue(final String pluginName, final LinkedList<Object> list, final Speech speech) {
        this.pluginName = pluginName;
        queue = list;
        this.speech = speech;
    }

    public PluginQueue(final Parcel parcel) {
        pluginName = parcel.readString();
        startBroadcast = parcel.readString();
        stopBroadcast = parcel.readString();
        queue = new LinkedList<Object>();
        parcel.readList(queue, null);
        // speech = parcel.readParcelable(null);
        // speech = (Speech) parcel.readValue(Speech.class.getClassLoader());
    }

    public Object getNext() {
        return queue.poll();
    }

    public String sniffNextString() {
        for (final Object o : queue) {
            if (o instanceof String) {
                return (String)o;
            }
        }
        return "";
    }

    public boolean isEmpty() {
        return queue.size() == 0 ? true : false;
    }

    public String getPluginName() {
        return pluginName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(pluginName);
        dest.writeString(startBroadcast);
        dest.writeString(stopBroadcast);
        dest.writeList(queue);
        // dest.writeValue(speech);
    }

    public String getStartBroadcast() {
        if (startBroadcast == null) {
            return "";
        }
        return startBroadcast;
    }

    public String getStopBroadcast() {
        if (stopBroadcast == null) {
            return "";
        }
        return stopBroadcast;
    }

    public Speech getSpeech() {
        return speech;
    }

    public void setSpeech(final Speech speech) {
        this.speech = speech;
    }

    public void sendToService(final Context context, final int priority) {
        final Intent announceIntent = new Intent(PluginService.ACTION_ANNOUNCE);
        final Bundle bundle = new Bundle();
        bundle.putInt(PluginService.EXTRA_PRIORITY, priority);
        bundle.putParcelable(PluginService.EXTRA_QUEUE, this);
        announceIntent.putExtras(bundle);
        context.startService(announceIntent);
    }
}
