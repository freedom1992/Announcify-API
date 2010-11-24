package com.announcify.sql.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.announcify.sql.AnnouncifyDatabase;

public class PluginModel implements BaseModel, BaseColumns {
	public static final String TABLE_NAME = "Plugin";

	public static final String KEY_PLUGIN_NAME = "name";
	public static final String KEY_PLUGIN_ACTIVE = "active";

	private final SQLiteDatabase database;

	public PluginModel(final Context context) {
		database = new AnnouncifyDatabase(context).getWritableDatabase();
	}

	public void togglePlugin(final int id) {
		final ContentValues values = new ContentValues();
		final Cursor cursor = get(id);
		cursor.moveToFirst();
		values.put(KEY_PLUGIN_ACTIVE, cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE)) == 1 ? 0 : 1);
		cursor.close();
		database.update(TABLE_NAME, values, _ID + " = " + id, null);
	}

	public Cursor getAll() {
		return database.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public void remove(final int id) {
		database.delete(TABLE_NAME, BaseColumns._ID + " = " + id, null);
	}

	public Cursor get(final int id) {
		return database.query(TABLE_NAME, null, BaseColumns._ID + " = " + id, null, null, null, null);
	}

	public int getId(final String name) {
		final Cursor cursor = database.query(TABLE_NAME, null, KEY_PLUGIN_NAME + " = " + "'" + name + "'", null, null, null, null);
		cursor.moveToFirst();
		final int i = cursor.getInt(cursor.getColumnIndex(_ID));
		cursor.close();
		return i;
	}

	public boolean getActive(final String name) {
		final Cursor cursor = database.query(TABLE_NAME, null, KEY_PLUGIN_NAME + " = " + "'" + name + "'", null, null, null, null);
		cursor.moveToFirst();
		final int i = cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE));
		cursor.close();
		return i == 1 ? true : false;
	}

	public void close() {
		database.close();
	}

	public void add(final String name) {
		final ContentValues values = new ContentValues();
		values.put(KEY_PLUGIN_NAME, name);
		values.put(KEY_PLUGIN_ACTIVE, 1);
		database.insert(TABLE_NAME, null, values);
	}
}