package com.announcify.api.background.sql.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GroupModel extends BaseModel {

    public final static String TABLE_NAME = "Contact_Group";

    public static final String KEY_GROUP_ID = "group_id";

    public static final String KEY_GROUP_TITLE = "group_title";

    public GroupModel(final Context context) {
        super(context, TABLE_NAME);
    }

    public void add(final long groupId, final String title) {
        final ContentValues values = new ContentValues();
        values.put(KEY_GROUP_ID, groupId);
        values.put(KEY_GROUP_TITLE, title);
        getResolver().insert(buildUri(), values);
    }

    public boolean getEnabled(final long groupId) {
        final Cursor cursor = getResolver().query(buildUri(), null, KEY_GROUP_ID + " = ?", new String[] { String.valueOf(groupId) }, null);

        try {
            return cursor.moveToFirst();
        } finally {
            cursor.close();
        }
    }

    @Override
    public void remove(final long groupId) {
        getResolver().delete(buildUri(), _ID + " = ?", new String[] { String.valueOf(groupId) });
    }
}
