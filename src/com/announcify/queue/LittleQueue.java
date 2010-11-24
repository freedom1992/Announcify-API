package com.announcify.queue;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;

public class LittleQueue {
	private final LinkedList<Object> queue;

	private final Context context;
	private final Intent startBroadcast;
	private final Intent stopBroadcast;

	private final String pluginName;

	public LittleQueue(final Context context, final String pluginName, final String startBroadcast, final String stopBroadcast, final LinkedList<Object> queue) {
		this.context = context;
		this.pluginName = pluginName;
		this.startBroadcast = new Intent(startBroadcast);
		this.stopBroadcast = new Intent(stopBroadcast);
		this.queue = queue;
	}

	public void start() {
		context.sendBroadcast(startBroadcast);
	}

	public Object getNext() {
		return queue.poll();
	}

	public void stop() {
		context.sendBroadcast(stopBroadcast);
	}

	public boolean isEmpty() {
		return queue.size() == 0 ? true : false;
	}

	public String getPluginName() {
		return pluginName;
	}
}