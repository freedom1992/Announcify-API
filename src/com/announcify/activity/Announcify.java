package com.announcify.activity;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.announcify.R;
import com.announcify.activity.widget.PluginAdapter;
import com.announcify.sql.model.PluginModel;

public class Announcify extends ListActivity {
	public static final String PERMISSION_IM_A_PLUGIN = "com.announcify.PERMISSION_IM_A_PLUGIN";

	public static final String ACTION_PLUGIN_CONTACT = "com.announcify.ACTION_PLUGIN_CONTACT";
	public static final String ACTION_PLUGIN_RESPOND = "com.announcify.ACTION_PLUGIN_RESPOND";

	public static final String EXTRA_PLUGIN_NAME = "com.announcify.EXTRA_PLUGIN_NAME";
	public static final String EXTRA_PLUGIN_ACTION = "com.announcify.EXTRA_PLUGIN_INTENT";

	private CheckedTextView header;
	private PluginAdapter adapter;
	private PluginModel model;
	private PluginReceiver pluginReceiver;

	private boolean registered;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);

		findViewById(R.id.button_more).setOnClickListener(new OnClickListener() {

			public void onClick(final View v) {
				openOptionsMenu();
			}
		});

		pluginReceiver = new PluginReceiver();
		registerReceiver(pluginReceiver, new IntentFilter(ACTION_PLUGIN_RESPOND));
		registered = true;
		sendBroadcast(new Intent(ACTION_PLUGIN_CONTACT));

		model = new PluginModel(this);

		header = (CheckedTextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_checked, null);
		header.setText("Announcify");
		header.setChecked(model.getActive("Announcify"));

		getListView().addHeaderView(header);

		adapter = new PluginAdapter(this, model);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v, int position, final long id) {
		if (position == 0) {
			if (model.getActive("Announcify")) {
				model.togglePlugin(1);
				header.setChecked(false);
				setListAdapter(null);
			} else {
				model.togglePlugin(1);
				header.setChecked(true);
				setListAdapter(adapter);
			}
		}

		switch (adapter.getItemViewType(--position)) {
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
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// TODO: do something.
		return true;
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
				final Intent receivedIntent = new Intent(intent.getStringExtra(EXTRA_PLUGIN_ACTION));
				// receivedIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				adapter.add(intent.getStringExtra(EXTRA_PLUGIN_NAME), receivedIntent);
			} else {
				Log.d(Announcify.class.getSimpleName(), "strange intent, sorry!");
			}
		}
	}
}