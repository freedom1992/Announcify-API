package com.announcify.api.background.sql.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ContactModel extends BaseModel {

    public final static String TABLE_NAME = "Contact";
    public static final String KEY_CONTACT_ID = "contact_id";
    public static final String KEY_CONTACT_TITLE = "contact_title";

    public ContactModel(final Context context) {
        super(context, TABLE_NAME);
    }

    public void add(final String key, final String title) {
        final ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_ID, key);
        values.put(KEY_CONTACT_TITLE, title);
        getResolver().insert(buildUri(), values);
    }

    public boolean getEnabled(final String key) {
        final Cursor cursor = getResolver().query(buildUri(), null, KEY_CONTACT_ID + " = ?", new String[] { key }, null);

        try {
            return cursor.moveToFirst();
        } finally {
            cursor.close();
        }
    }

    public void remove(final String key) {
        getResolver().delete(buildUri(), _ID + " = ?", new String[] { key });
    }
}
