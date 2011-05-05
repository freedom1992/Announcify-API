package com.announcify.api.background.contact.lookup;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;

import com.announcify.api.background.contact.Contact;

public class Mail implements LookupMethod {

    public static void prepareAddress(final Contact contact, final boolean cheat) {
        if (contact.getAddress().contains("\"") && cheat) {
            contact.setFullname(contact.getAddress().substring(1, contact.getAddress().lastIndexOf("\"")));

            contact.setLookupString("com.announcify.CHEAT");
            // little hack to avoid announcing this contact as unknown, if he's
            // not in addressbook!
        }

        if (contact.getAddress().contains("<") && cheat) {
            contact.setAddress(contact.getAddress().substring(contact.getAddress().indexOf('<') + 1, contact.getAddress().indexOf('>')));
        }
    }

    private final Context context;
    private Contact contact;

    private final boolean cheat;

    public Mail(final Context context, final boolean cheat) {
        this.context = context;
        this.cheat = cheat;
    }

    public void getAddress() {
        if (contact.getAddress() == null || contact.getLookupString() == null) {
            return;
        }

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Email.CONTENT_URI, new String[] { Email.DATA1 }, Contacts.LOOKUP_KEY + " = ?", new String[] { contact.getLookupString() }, null);
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

        prepareAddress(contact, cheat);

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, contact.getAddress()), new String[] { Email.LOOKUP_KEY }, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            contact.setLookupString(cursor.getString(0));
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
            cursor = context.getContentResolver().query(Email.CONTENT_URI, new String[] { Email.LABEL, Email.TYPE }, Email.LOOKUP_KEY + " = ? AND " + Email.DATA1 + " = ?", new String[] { contact.getLookupString(), contact.getAddress() }, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            String label = cursor.getString(cursor.getColumnIndex(Email.LABEL));
            if (label == null) {
                final int type = cursor.getInt(cursor.getColumnIndex(Email.TYPE)) - 1;
                final String[] types = Resources.getSystem().getStringArray(android.R.array.emailAddressTypes);
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
