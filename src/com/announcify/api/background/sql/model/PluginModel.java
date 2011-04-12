package com.announcify.api.background.sql.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

public class PluginModel extends BaseModel {

    public final static String TABLE_NAME = "Plugin";

    public static final String KEY_PLUGIN_NAME = "name";
    public static final String KEY_PLUGIN_PACKAGE = "package";
    public static final String KEY_PLUGIN_ACTION = "action";
    public static final String KEY_PLUGIN_ACTIVE = "active";
    public static final String KEY_PLUGIN_PRIORITY = "priority";

    public PluginModel(final Context context) {
        super(context, TABLE_NAME);
    }

    public void add(final String name, final int priority, final String action, final String packageString, final boolean broadcast) {
        final ContentValues values = new ContentValues();
        values.put(KEY_PLUGIN_NAME, name);
        values.put(KEY_PLUGIN_PACKAGE, packageString);
        values.put(KEY_PLUGIN_ACTIVE, 1);
        values.put(KEY_PLUGIN_PRIORITY, priority);
        values.put(KEY_PLUGIN_ACTION, action);

        resolver.insert(buildUri(), values);
    }

    public String getAction(final long id) {
        final Cursor cursor = resolver.query(buildUri(), new String[] { KEY_PLUGIN_ACTION }, _ID + " = ?", new String[] { String.valueOf(id) }, null);

        try {
            if (!cursor.moveToFirst()) {
                return "";
            }

            return cursor.getString(cursor.getColumnIndex(KEY_PLUGIN_ACTION));
        } finally {
            cursor.close();
        }
    }

    public boolean getActive(final long id) {
        final long announcifyId = getId("Announcify++");
        if (id != announcifyId && !getActive(announcifyId)) {
            return false;
        }

        final Cursor cursor = resolver.query(buildUri(), new String[] { KEY_PLUGIN_ACTIVE }, _ID + " = ?", new String[] { String.valueOf(id) }, null);

        try {
            if (!cursor.moveToFirst()) {
                return true; // Plugin not yet registered. Announce anyway (for
                // better user experience)
            }

            return cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE)) == 1 ? true : false;
        } finally {
            cursor.close();
        }
    }

    public boolean known(final long id) {
        final Cursor cursor = resolver.query(buildUri(), new String[] { KEY_PLUGIN_ACTIVE }, _ID + " = ?", new String[] { String.valueOf(id) }, null);

        try {
            if (!cursor.moveToFirst()) {
                return false;
            } else {
                return true;
            }
        } finally {
            cursor.close();
        }
    }

    public void setActive(final long id, final boolean enabled) {
        if (getActive(id) != enabled) {
            togglePlugin(id);
        }
    }

    @Override
    public Cursor getAll() {
        return resolver.query(buildUri(), null, null, null, KEY_PLUGIN_NAME);
    }

    public long getId(final String name) {
        final Cursor cursor = resolver.query(buildUri(), new String[] { _ID }, KEY_PLUGIN_NAME + " = ?", new String[] { String.valueOf(name) }, null);

        try {
            if (!cursor.moveToFirst()) {
                return -1;
            }

            return cursor.getLong(cursor.getColumnIndex(_ID));
        } finally {
            cursor.close();
        }
    }

    public String getName(final long id) {
        final Cursor cursor = resolver.query(buildUri(), new String[] { KEY_PLUGIN_NAME }, _ID + " = ?", new String[] { String.valueOf(id) }, null);

        try {
            if (!cursor.moveToFirst()) {
                return "";
            }

            return cursor.getString(cursor.getColumnIndex(KEY_PLUGIN_NAME));
        } finally {
            cursor.close();
        }
    }

    public String getPackage(final long id) {
        final Cursor cursor = resolver.query(buildUri(), new String[] { KEY_PLUGIN_PACKAGE }, _ID + " = ?", new String[] { String.valueOf(id) }, null);

        try {
            if (!cursor.moveToFirst()) {
                return "";
            }

            return cursor.getString(cursor.getColumnIndex(KEY_PLUGIN_PACKAGE));
        } finally {
            cursor.close();
        }
    }

    public int getPriority(final long id) {
        final Cursor cursor = resolver.query(buildUri(), new String[] { KEY_PLUGIN_PRIORITY }, _ID + " = ?", new String[] { String.valueOf(id) }, null);

        try {
            if (!cursor.moveToFirst()) {
                return 10;
            }

            return cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_PRIORITY));
        } finally {
            cursor.close();
        }
    }

    public void togglePlugin(final long id) {
        final Cursor cursor = get(id);

        try {
            if (!cursor.moveToFirst()) {
                return;
            }

            final ContentValues values = new ContentValues();
            values.put(KEY_PLUGIN_ACTIVE, cursor.getInt(cursor.getColumnIndex(KEY_PLUGIN_ACTIVE)) == 1 ? 0 : 1);

            resolver.update(buildUri(), values, _ID + " = ?", new String[] { String.valueOf(id) });
        } finally {
            cursor.close();
        }
    }

    public void update(final long id, final String name, final int priority, final String action, final String packageString, final boolean broadcast) {
        final ContentValues values = new ContentValues();
        values.put(KEY_PLUGIN_NAME, name);
        values.put(KEY_PLUGIN_PACKAGE, packageString);
        values.put(KEY_PLUGIN_PRIORITY, priority);
        values.put(KEY_PLUGIN_ACTION, action);
        values.put(KEY_PLUGIN_ACTIVE, getActive(id));

        resolver.update(buildUri(), values, BaseColumns._ID + " = ?", new String[] { String.valueOf(id) });
    }
}
