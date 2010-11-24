package com.announcify.activity.widget;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.announcify.sql.model.PluginModel;

public class PluginAdapter extends BaseAdapter {
	private final static int TYPES = 3;
	public final static int TYPE_HEADER = 0;
	public final static int TYPE_CHECKBOX = 1;
	public final static int TYPE_INTENT = 2;

	private final List<String> names = new LinkedList<String>();
	private final Map<String, Intent> intents = new HashMap<String, Intent>();

	private final LayoutInflater inflater;
	private final PluginModel model;

	public PluginAdapter(final Context context, final PluginModel model) {
		super();

		this.model = model;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void add(final String name, final Intent intent) {
		names.add(name);
		intents.put(name, intent);
		Collections.sort(names);
		notifyDataSetChanged();

		model.add(name);
	}

	@Override
	public int getItemViewType(final int position) {
		switch (position % 3) {
			case TYPE_HEADER:
				return TYPE_HEADER;
			case TYPE_CHECKBOX:
				return TYPE_CHECKBOX;
			case TYPE_INTENT:
				return TYPE_INTENT;
			default:
				return IGNORE_ITEM_VIEW_TYPE;
		}
	}

	@Override
	public int getViewTypeCount() {
		return TYPES;
	}

	public int getCount() {
		return names.size() * TYPES;
	}

	public Object getItem(final int position) {
		switch (getItemViewType(position)) {
			case TYPE_HEADER:
				return names.get(position / 3).charAt(0);
			case TYPE_CHECKBOX:
				return names.get(position / 3);
			case TYPE_INTENT:
				return names.get(position / 3) + " Settings";
			default:
				return null;
		}
	}

	public Intent getIntent(final int position) {
		return intents.get(names.get(position / 3));
	}

	public long getItemId(final int position) {
		return position;
	}

	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = null;
		switch (getItemViewType(position)) {
			case TYPE_HEADER:
				view = inflater.inflate(com.announcify.R.layout.list_header, null);
				((TextView) view).setText(getItem(position).toString());
				break;
			case TYPE_CHECKBOX:
				view = inflater.inflate(R.layout.simple_list_item_checked, null);
				final CheckedTextView check = (CheckedTextView) view;
				check.setText(getItem(position).toString());
				check.setChecked(model.getActive((String) getItem(position)));
				break;
			case TYPE_INTENT:
				view = inflater.inflate(R.layout.simple_list_item_1, null);
				((TextView) view).setText(getItem(position).toString());
				break;
		}
		return view;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(final int position) {
		return getItemViewType(position) != TYPE_HEADER;
	}
}