package com.announcify.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class AnnouncifyReceiver extends BroadcastReceiver {
	public static final String PERMISSION_IM_A_PLUGIN = "com.announcify.PERMISSION_IM_A_PLUGIN";

	public static final String ACTION_PLUGIN_CONTACT = "com.announcify.ACTION_PLUGIN_CONTACT";
	public static final String ACTION_PLUGIN_RESPOND = "com.announcify.ACTION_PLUGIN_RESPOND";

	public static final String EXTRA_PLUGIN_NAME = "com.announcify.EXTRA_PLUGIN_NAME";
	public static final String EXTRA_PLUGIN_ACTION = "com.announcify.EXTRA_PLUGIN_ACTION";

	@Override
	abstract public void onReceive(Context context, Intent intent);
}