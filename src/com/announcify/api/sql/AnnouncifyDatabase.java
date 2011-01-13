
package com.announcify.api.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.announcify.api.sql.model.PluginModel;
import com.announcify.api.sql.model.TranslationModel;

public class AnnouncifyDatabase extends SQLiteOpenHelper {
    public static AnnouncifyDatabase getDatabase(Context context) {
        try {
            if (!"com.announcify".equals(context.getPackageName())) {
                context = context.createPackageContext("com.announcify", 0);
            }
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }

        return new AnnouncifyDatabase(context);
    }

    private AnnouncifyDatabase(final Context context) {
        super(context, "announcify.db", null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PluginModel.TABLE_NAME + " (" + BaseColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + PluginModel.KEY_PLUGIN_PACKAGE
                + " TEXT NOT NULL," + PluginModel.KEY_PLUGIN_NAME + " TEXT UNIQUE NOT NULL,"
                + PluginModel.KEY_PLUGIN_PRIORITY + " INTEGER," + PluginModel.KEY_PLUGIN_ACTION
                + " TEXT NOT NULL," + PluginModel.KEY_PLUGIN_BROADCAST + " INTEGER,"
                + PluginModel.KEY_PLUGIN_TIMER + " INTEGER," + PluginModel.KEY_PLUGIN_ACTIVE
                + " INTEGER);");

        db.execSQL("CREATE TABLE " + TranslationModel.TABLE_NAME + " (" + BaseColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + TranslationModel.KEY_PLUGIN_FROM
                + " TEXT," + TranslationModel.KEY_PLUGIN_TO + " TEXT,"
                + TranslationModel.KEY_PLUGIN_DESTINATION + " TEXT,"
                + TranslationModel.KEY_PLUGIN_SOURCE + " TEXT);");

        final ContentValues values = new ContentValues();
        values.put(PluginModel.KEY_PLUGIN_NAME, "Announcify++");
        values.put(PluginModel.KEY_PLUGIN_ACTIVE, 1);
        values.put(PluginModel.KEY_PLUGIN_PRIORITY, 0);
        values.put(PluginModel.KEY_PLUGIN_PACKAGE, "com.announcify");
        values.put(PluginModel.KEY_PLUGIN_ACTION, "com.announcify.SETTINGS");
        db.insert(PluginModel.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.e("smn", "onUpgrade");
    }
}
