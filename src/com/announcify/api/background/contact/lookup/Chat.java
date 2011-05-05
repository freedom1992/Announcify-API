package com.announcify.api.background.contact.lookup;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Im;

import com.announcify.api.background.contact.Contact;

public class Chat implements LookupMethod {

    private final Context context;
    private Contact contact;

    public Chat(final Context context) {
        this.context = context;
    }

    public void getAddress() {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { Im.DATA1 }, Im.LOOKUP_KEY + " = ? AND " + Im.MIMETYPE + " = ?", new String[] { contact.getLookupString(), Im.CONTENT_ITEM_TYPE }, null);
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
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { Im.LOOKUP_KEY },
            // Im.DATA1 + " = ? AND " + Im.MIMETYPE + " = ?",
            Im.DATA1 + " = ?", new String[] { contact.getAddress() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setLookupString(cursor.getString(cursor.getColumnIndex(Im.LOOKUP_KEY)));
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
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { Im.TYPE }, Im.LOOKUP_KEY + " = ? AND " + Data.MIMETYPE + " = ? AND " + Im.DATA1 + " = ?", new String[] { contact.getLookupString(), Im.CONTENT_ITEM_TYPE, contact.getAddress() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            String label = cursor.getString(cursor.getColumnIndex(Im.LABEL));
            if (label == null) {
                final int type = cursor.getInt(cursor.getColumnIndex(Im.TYPE)) - 1;
                final String[] types = Resources.getSystem().getStringArray(android.R.array.imProtocols);
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
