package com.announcify.api.background.queue;

import java.util.LinkedList;

import android.os.Parcel;
import android.os.Parcelable;

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

    private final LinkedList<Object> queue;
    private final String pluginName;
    private final String startBroadcast;
    private final String stopBroadcast;

    public PluginQueue(final Parcel parcel) {
        pluginName = parcel.readString();
        startBroadcast = parcel.readString();
        stopBroadcast = parcel.readString();
        queue = new LinkedList<Object>();
        parcel.readList(queue, null);
    }

    public PluginQueue(final String pluginName, final LinkedList<Object> list) {
        this(pluginName, list, "", "");
    }

    public PluginQueue(final String pluginName, final LinkedList<Object> list, final String startBroadcast, final String stopBroadcast) {
        this.pluginName = pluginName;
        queue = list;
        this.startBroadcast = startBroadcast;
        this.stopBroadcast = stopBroadcast;
    }

    public int describeContents() {
        return 0;
    }

    public Object getNext() {
        return queue.poll();
    }

    public String getPluginName() {
        return pluginName;
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

    public boolean isEmpty() {
        return queue.size() == 0 ? true : false;
    }

    public String sniffNextString() {
        for (final Object o : queue) {
            if (o instanceof String) {
                return (String) o;
            }
        }
        return "";
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(pluginName);
        dest.writeString(startBroadcast);
        dest.writeString(stopBroadcast);
        dest.writeList(queue);
    }
}
