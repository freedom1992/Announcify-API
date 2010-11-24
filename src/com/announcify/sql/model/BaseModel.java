package com.announcify.sql.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.announcify.sql.AnnouncifyDatabase;

public class BaseModel implements BaseColumns {
	protected String TABLE_NAME;
	protected final SQLiteDatabase database;

	public BaseModel(final Context context, final String table) {
		TABLE_NAME = table;
		database = new AnnouncifyDatabase(context).getWritableDatabase();
	}

	public Cursor get(final int id) {
		return database.query(TABLE_NAME, null, BaseColumns._ID + " = " + id, null, null, null, null);
	}

	public Cursor getAll() {
		return database.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public void remove(final int id) {
		database.delete(TABLE_NAME, BaseColumns._ID + " = " + id, null);
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void close() {
		database.close();
	}
}
