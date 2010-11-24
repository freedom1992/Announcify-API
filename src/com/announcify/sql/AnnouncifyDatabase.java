package com.announcify.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.announcify.sql.model.PluginModel;

public class AnnouncifyDatabase extends SQLiteOpenHelper {
	private final Context context;

	public AnnouncifyDatabase(final Context context) {
		super(context, "announcify.db", null, 1);

		this.context = context;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL("create table " + PluginModel.TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PluginModel.KEY_PLUGIN_NAME + " TEXT UNIQUE NOT NULL," + PluginModel.KEY_PLUGIN_ACTIVE + " INTEGER);");

		final ContentValues values = new ContentValues();
		values.put(PluginModel.KEY_PLUGIN_NAME, "Announcify");
		values.put(PluginModel.KEY_PLUGIN_ACTIVE, 1);
		db.insert(PluginModel.TABLE_NAME, null, values);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.e("smn", "onUpgrade?");
	}
}
