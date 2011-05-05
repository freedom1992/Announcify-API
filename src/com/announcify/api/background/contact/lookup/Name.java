package com.announcify.api.background.contact.lookup;

import java.util.LinkedList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

import com.announcify.api.background.contact.Contact;

public class Name {

    public static void getFirstname(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { StructuredName.GIVEN_NAME }, StructuredName.MIMETYPE + " = ? AND " + StructuredName.LOOKUP_KEY + " = ?", new String[] { StructuredName.CONTENT_ITEM_TYPE, contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setFirstname(cursor.getString(0));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void getFullname(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { StructuredName.DISPLAY_NAME }, StructuredName.MIMETYPE + " = ? AND " + StructuredName.LOOKUP_KEY + " = ?", new String[] { StructuredName.CONTENT_ITEM_TYPE, contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setFullname(cursor.getString(0));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void getGroups(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { Data.IN_VISIBLE_GROUP }, Data.LOOKUP_KEY + " = ?", new String[] { contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            final LinkedList<Long> groups = new LinkedList<Long>();
            do {
                groups.add(cursor.getLong(0));
            } while (cursor.moveToNext());
            contact.setGroups(groups);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void getLastname(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { StructuredName.FAMILY_NAME }, StructuredName.MIMETYPE + " = ? AND " + StructuredName.LOOKUP_KEY + " = ?", new String[] { StructuredName.CONTENT_ITEM_TYPE, contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setLastname(cursor.getString(0));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void getMiddlename(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { StructuredName.MIDDLE_NAME }, StructuredName.MIMETYPE + " = ? AND " + StructuredName.LOOKUP_KEY + " = ?", new String[] { StructuredName.CONTENT_ITEM_TYPE, contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            // TODO: implement?
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void getNickname(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { Nickname.NAME }, Nickname.MIMETYPE + " = ? AND " + Nickname.LOOKUP_KEY + " = ?", new String[] { Nickname.CONTENT_ITEM_TYPE, contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setNickname(cursor.getString(0));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void getPrefix(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { StructuredName.PREFIX }, StructuredName.MIMETYPE + " = ? AND " + StructuredName.LOOKUP_KEY + " = ?", new String[] { StructuredName.CONTENT_ITEM_TYPE, contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setTitle(cursor.getString(0));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void getSuffix(final Context context, final Contact contact) {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { StructuredName.SUFFIX }, StructuredName.MIMETYPE + " = ? AND " + StructuredName.LOOKUP_KEY + " = ?", new String[] { StructuredName.CONTENT_ITEM_TYPE, contact.getLookupString() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            // TODO: implement?
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
