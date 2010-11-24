package com.announcify.activity;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.announcify.activity.widget.PluginAdapter;
import com.announcify.sql.model.PluginModel;

public class Announcify extends ListActivity {
	public static final String PERMISSION_IM_A_PLUGIN = "com.announcify.PERMISSION_IM_A_PLUGIN";

	public static final String ACTION_PLUGIN_CONTACT = "com.announcify.ACTION_PLUGIN_CONTACT";
	public static final String ACTION_PLUGIN_RESPOND = "com.announcify.ACTION_PLUGIN_RESPOND";

	public static final String EXTRA_PLUGIN_NAME = "com.announcify.EXTRA_PLUGIN_NAME";
	public static final String EXTRA_PLUGIN_ACTION = "com.announcify.EXTRA_PLUGIN_INTENT";

	private PluginAdapter adapter;
	private PluginModel model;
	private PluginReceiver pluginReceiver;

	private boolean registered;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pluginReceiver = new PluginReceiver();
		registerReceiver(pluginReceiver, new IntentFilter(ACTION_PLUGIN_RESPOND));
		registered = true;
		sendBroadcast(new Intent(ACTION_PLUGIN_CONTACT));

		model = new PluginModel(this);

		adapter = new PluginAdapter(this, model);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
		switch (adapter.getItemViewType(position)) {
			case PluginAdapter.TYPE_INTENT:
				startActivity(adapter.getIntent(position));
				break;
			case PluginAdapter.TYPE_CHECKBOX:
				model.togglePlugin(model.getId((String) adapter.getItem(position)));
				adapter.notifyDataSetChanged();
				break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (pluginReceiver != null && registered) {
			unregisterReceiver(pluginReceiver);
			registered = false;
		}
	}

	private class PluginReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (intent != null && intent.getExtras() != null && intent.getStringExtra(EXTRA_PLUGIN_NAME) != null && !"".equals(intent.getStringExtra(EXTRA_PLUGIN_NAME))) {
				adapter.add(intent.getStringExtra(EXTRA_PLUGIN_NAME), new Intent(intent.getStringExtra(EXTRA_PLUGIN_ACTION)));
			} else {
				Log.d(Announcify.class.getSimpleName(), "strange intent, sorry!");
			}
		}
	}
}