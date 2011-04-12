package com.announcify.api.background.sql.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class BaseModel implements BaseColumns {

    public static final Uri PROVIDER_URI = Uri.parse("content://com.announcify");

    protected final String TABLE_NAME;

    protected final ContentResolver resolver;

    public BaseModel(final Context context, final String table) {
        TABLE_NAME = table;
        resolver = context.getContentResolver();
    }

    public Uri buildUri() {
        return Uri.withAppendedPath(PROVIDER_URI, TABLE_NAME);
    }

    public Cursor get(final long id) {
        return resolver.query(buildUri(), null, BaseColumns._ID + " = ?", new String[] { String.valueOf(id) }, null);
    }

    public Cursor getAll() {
        return resolver.query(buildUri(), null, null, null, null);
    }

    public Cursor getAll(final String orderBy) {
        return resolver.query(buildUri(), null, null, null, orderBy);
    }

    public ContentResolver getResolver() {
        return resolver;
    }

    public void remove(final long id) {
        resolver.delete(buildUri(), BaseColumns._ID + " = ?", new String[] { String.valueOf(id) });
    }
}
