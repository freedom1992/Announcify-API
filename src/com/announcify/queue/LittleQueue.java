package com.announcify.queue;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.announcify.service.AnnouncifyService;
import com.announcify.tts.Speech;

public class LittleQueue implements Parcelable {
	// this MUST be a public static final variable!!
	public static final Creator<LittleQueue> CREATOR = new Creator<LittleQueue>() {
		public LittleQueue createFromParcel(final Parcel source) {
			// TODO: called twice
			return new LittleQueue(source);
		}

		public LittleQueue[] newArray(final int size) {
			return new LittleQueue[size];
		}
	};

	private final LinkedList<Object> queue;

	private final String pluginName;
	private String startBroadcast;
	private String stopBroadcast;
	private Speech speech;

	public LittleQueue(final String pluginName, final LinkedList<Object> list, final Context context) {
		this.pluginName = pluginName;
		queue = list;
		startBroadcast = "";
		stopBroadcast = "";
		speech = new Speech(context);
	}

	public LittleQueue(final String pluginName, final LinkedList<Object> list, final String startBroadcast, final String stopBroadcast, final Context context) {
		this.pluginName = pluginName;
		queue = list;
		this.startBroadcast = startBroadcast;
		this.stopBroadcast = stopBroadcast;
		speech = new Speech(context);
	}

	public LittleQueue(final String pluginName, final LinkedList<Object> list, final String startBroadcast, final String stopBroadcast, final Speech speech) {
		this.pluginName = pluginName;
		queue = list;
		this.startBroadcast = startBroadcast;
		this.stopBroadcast = stopBroadcast;
		this.speech = speech;
	}

	public LittleQueue(final String pluginName, final LinkedList<Object> list, final Speech speech) {
		this.pluginName = pluginName;
		queue = list;
		this.speech = speech;
	}

	public LittleQueue(final Parcel parcel) {
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
				return (String) o;
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
		final Intent announceIntent = new Intent(AnnouncifyService.ACTION_ANNOUNCE);
		final Bundle bundle = new Bundle();
		bundle.putInt(AnnouncifyService.EXTRA_PRIORITY, priority);
		bundle.putParcelable(AnnouncifyService.EXTRA_QUEUE, this);
		announceIntent.putExtras(bundle);
		context.startService(announceIntent);
	}
}