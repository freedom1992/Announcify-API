
package com.announcify.api.sql.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.announcify.api.sql.AnnouncifyDatabase;

public class BaseModel implements BaseColumns {
    protected String TABLE_NAME;

    protected final AnnouncifyDatabase announcify;
    protected final SQLiteDatabase database;

    
    public BaseModel(final Context context, final String table) {
        TABLE_NAME = table;
        announcify = AnnouncifyDatabase.getDatabase(context);
        database = announcify.getWritableDatabase();
    }
    

    public Cursor get(final long id) {
            return database.query(TABLE_NAME, null, BaseColumns._ID + " = ?", new String[] {
                String.valueOf(id)
            }, null, null, null);
    }

    public Cursor getAll() {
            return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void remove(final long id) {
            database.delete(TABLE_NAME, BaseColumns._ID + " = ?", new String[] {
                String.valueOf(id)
            });
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void close() {
        database.close();
        announcify.close();
    }
}
