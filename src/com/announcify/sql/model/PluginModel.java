package com.announcify.sql.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PluginModel extends BaseModel {
	public final static String TABLE_NAME = "Plugin";

	public static final String KEY_PLUGIN_NAME = "Name";
	public static final String KEY_PLUGIN_ACTIVE = "Active";

	public PluginModel(final Context context) {
		super(context, TABLE_NAME);
	}

	public void togglePlugin(final int id) {
		final ContentValues values = new ContentValues();
		final Cursor cursor = get(id);
		cursor.moveToFirst();
		values.put(KEY_PLUGIN_ACTIVE, cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE)) == 1 ? 0 : 1);
		cursor.close();
		database.update(TABLE_NAME, values, _ID + " = " + id, null);
	}

	public int getId(final String name) {
		final Cursor cursor = database.query(TABLE_NAME, null, KEY_PLUGIN_NAME + " = " + "'" + name + "'", null, null, null, null);
		cursor.moveToFirst();
		final int i = cursor.getInt(cursor.getColumnIndex(_ID));
		cursor.close();
		return i;
	}

	public boolean getActive(final String name) {
		Cursor cursor = database.query(TABLE_NAME, null, KEY_PLUGIN_NAME + " = " + "'Announcify'", null, null, null, null);
		cursor.moveToFirst();
		if (cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE)) != 1) {
			cursor.close();
			return false;
		} else {
			cursor.close();
		}

		cursor = database.query(TABLE_NAME, null, KEY_PLUGIN_NAME + " = " + "'" + name + "'", null, null, null, null);
		cursor.moveToFirst();
		final int i = cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE));
		cursor.close();
		return i == 1 ? true : false;
	}

	public void add(final String name) {
		final ContentValues values = new ContentValues();
		values.put(KEY_PLUGIN_NAME, name);
		values.put(KEY_PLUGIN_ACTIVE, 1);
		database.insert(TABLE_NAME, null, values);
	}
}