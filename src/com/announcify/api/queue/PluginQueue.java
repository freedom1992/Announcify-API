
package com.announcify.api.queue;

import java.util.LinkedList;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.announcify.api.tts.Speech;

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
}
