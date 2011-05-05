package com.announcify.api.background.contact.lookup;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Im;

import com.announcify.api.background.contact.Contact;

public class Twitter implements LookupMethod {

    private final Context context;
    private Contact contact;

    public Twitter(final Context context) {
        this.context = context;
    }

    public void getAddress() {
        // TODO Auto-generated method stub
    }

    public void getLookup(final Contact contact) {
        this.contact = contact;

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI, new String[] { Im.LOOKUP_KEY }, Im.DATA1 + " = ?", new String[] { contact.getAddress() }, null);
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
        // TODO Auto-generated method stub

    }

}
