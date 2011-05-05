package com.announcify.api.background.contact.lookup;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.announcify.api.background.contact.Contact;

public class Number implements LookupMethod {

    private final Context context;
    private Contact contact;

    public Number(final Context context) {
        this.context = context;
    }

    public void getAddress() {
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { CommonDataKinds.Phone.NUMBER }, Contacts.LOOKUP_KEY + " = ?", new String[] { contact.getLookupString() }, null);
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

    public void getLookup(final Contact contact) {
        this.contact = contact;

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contact.getAddress())), new String[] { PhoneLookup.LOOKUP_KEY }, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setLookupString(cursor.getString(cursor.getColumnIndex(PhoneLookup.LOOKUP_KEY)));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void getType() {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { CommonDataKinds.Phone.LABEL, CommonDataKinds.Phone.TYPE }, Contacts.LOOKUP_KEY + " = ? AND " + CommonDataKinds.Phone.NUMBER + " = ?", new String[] { contact.getLookupString(), contact.getAddress() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            String label = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.LABEL));
            if (label == null) {
                final int type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE)) - 1;
                final String[] types = Resources.getSystem().getStringArray(android.R.array.phoneTypes);
                if (types.length <= type) {
                    return;
                }

                label = types[type];
            }
            contact.setType(label);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
