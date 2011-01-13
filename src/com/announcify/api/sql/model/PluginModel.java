
package com.announcify.api.sql.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PluginModel extends BaseModel {
    public final static String TABLE_NAME = "Plugin";

    public static final String KEY_PLUGIN_NAME = "name";

    public static final String KEY_PLUGIN_PACKAGE = "package";

    public static final String KEY_PLUGIN_ACTION = "action";

    public static final String KEY_PLUGIN_BROADCAST = "broadcast";

    public static final String KEY_PLUGIN_ACTIVE = "active";

    public static final String KEY_PLUGIN_PRIORITY = "priority";

    public static final String KEY_PLUGIN_TIMER = "flush_timer";

    private final Context context;

    public PluginModel(final Context context) {
        super(context, TABLE_NAME);

        this.context = context;
    }

    public void togglePlugin(final int id) {
        final Cursor cursor = get(id);

        try {
            if (!cursor.moveToFirst()) {
                return;
            }

            final ContentValues values = new ContentValues();
            values.put(KEY_PLUGIN_ACTIVE,
                    cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE)) == 1 ? 0 : 1);

            database.update(TABLE_NAME, values, _ID + " = " + id, null);
        } finally {
            cursor.close();
        }
    }

    public int getId(final String name) {
        final Cursor cursor = database.query(TABLE_NAME, null, KEY_PLUGIN_NAME + " = " + "'" + name
                + "'", null, null, null, null);

        try {
            if (!cursor.moveToFirst()) {
                return -1;
            }

            return cursor.getInt(cursor.getColumnIndex(_ID));
        } finally {
            cursor.close();
        }
    }

    public boolean getActive(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return false;
            }

            return cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE)) == 1 ? true : false;
        } finally {
            cursor.close();
        }
    }

    /**
     * @returns True if dataset changed / GUI needs to be update its content.
     */
    public boolean increaseTimer(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return false;
            }

            final int i = cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_TIMER));
            if (i >= 2) {
                getDatabase().delete(TABLE_NAME, _ID + " = " + id, null);
                return true;
            } else {
                final ContentValues values = new ContentValues();
                values.put(KEY_PLUGIN_TIMER, i + 1);
                getDatabase().update(TABLE_NAME, values, _ID + " = " + id, null);
                return false;
            }
        } finally {
            cursor.close();
        }
    }

    public void clearTimer(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return;
            }

            final ContentValues values = new ContentValues();
            values.put(KEY_PLUGIN_TIMER, 0);
            getDatabase().update(TABLE_NAME, values, _ID + " = " + id, null);
        } finally {
            cursor.close();
        }
    }

    public int getPriority(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return 10;
            }

            return cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_PRIORITY));
        } finally {
            cursor.close();
        }
    }

    public String getName(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return "";
            }

            return cursor.getString(cursor.getColumnIndex(KEY_PLUGIN_NAME));
        } finally {
            cursor.close();
        }
    }

    public String getPackage(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return "";
            }

            return cursor.getString(cursor.getColumnIndex(KEY_PLUGIN_PACKAGE));
        } finally {
            cursor.close();
        }
    }

    public String getAction(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return "";
            }

            return cursor.getString(cursor.getColumnIndex(KEY_PLUGIN_ACTION));
        } finally {
            cursor.close();
        }
    }

    public boolean getBroadcast(final int id) {
        final Cursor cursor = database.query(TABLE_NAME, null, _ID + " = " + id, null, null, null,
                null);

        try {
            if (!cursor.moveToFirst()) {
                return false;
            }

            return cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_BROADCAST)) == 1 ? true : false;
        } finally {
            cursor.close();
        }
    }

    public void add(final String name, final int priority, final String action,
            final String packageString, final boolean broadcast) {
        final ContentValues values = new ContentValues();
        values.put(KEY_PLUGIN_NAME, name);
        values.put(KEY_PLUGIN_PACKAGE, packageString);
        values.put(KEY_PLUGIN_ACTIVE, 1);
        values.put(KEY_PLUGIN_PRIORITY, priority);
        values.put(KEY_PLUGIN_ACTION, action);
        values.put(KEY_PLUGIN_BROADCAST, broadcast ? 1 : 0);
        values.put(KEY_PLUGIN_TIMER, 0);
        database.insert(TABLE_NAME, null, values);
    }
}
