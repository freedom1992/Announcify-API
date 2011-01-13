
package com.announcify.api.contact.lookup;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;

import com.announcify.api.contact.Contact;

public class Call implements LookupMethod {
    private final Context context;

    private Contact contact;

    public Call(final Context context) {
        this.context = context;
    }

    public void getLookup(final Contact contact) {
        this.contact = contact;

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(
                    Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(contact.getAddress())), new String[] {
                        PhoneLookup.LOOKUP_KEY
                    }, null, null, null);
            if (!cursor.moveToFirst()) {
                return;
            }

            contact.setLookupString(cursor.getString(cursor.getColumnIndex(PhoneLookup.LOOKUP_KEY)));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void getAddress() {
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] {
                CommonDataKinds.Phone.NUMBER
            }, Contacts.LOOKUP_KEY + " = ?", new String[] {
                contact.getLookupString()
            }, null);
            if (!cursor.moveToFirst()) {
                return;
            }

            // TODO: implement?
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void getType() {
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] {
                    CommonDataKinds.Phone.LABEL, CommonDataKinds.Phone.TYPE
            }, Contacts.LOOKUP_KEY + " = ? AND " + CommonDataKinds.Phone.NUMBER + " = ?",
                    new String[] {
                            contact.getLookupString(), contact.getAddress()
                    }, null);
            if (!cursor.moveToFirst()) {
                return;
            }

            String label = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.LABEL));
            if (label == null) {
                label = Resources.getSystem().getStringArray(android.R.array.phoneTypes)[cursor
                        .getInt(cursor.getColumnIndex(Phone.TYPE)) - 1];
            }
            contact.setType(label);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
