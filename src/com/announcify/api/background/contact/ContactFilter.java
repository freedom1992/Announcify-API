package com.announcify.api.background.contact;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.announcify.api.background.contact.lookup.Name;
import com.announcify.api.background.sql.model.ContactModel;
import com.announcify.api.background.sql.model.GroupModel;
import com.announcify.api.background.util.AnnouncifySettings;

public class ContactFilter {

    /**
     * @return Returns true if you shouldn't announce this contact. Be sure to
     *         really do so! Otherwise the user might get angry and throw yellow
     *         birds at your house!
     */
    public static boolean announcableContact(final Context context, final Contact contact) {
        final AnnouncifySettings settings = new AnnouncifySettings(context);

        boolean announcable = filterGroup(context, contact, settings);
        announcable = announcable && filterContact(context, contact, settings);
        return announcable;
    }

    public static boolean filterGroup(final Context context, final Contact contact, final AnnouncifySettings settings) {
        if (!settings.getFilterByGroup()) {
            return true;
        }

        final boolean block = settings.getBlockGroups();

        Name.getGroups(context, contact);
        final List<Long> groups = contact.getGroups();

        final GroupModel model = new GroupModel(context);
        final Cursor cursor = model.getAll();

        try {
            final int index = cursor.getColumnIndex(GroupModel.KEY_GROUP_ID);

            while (cursor.moveToNext()) {
                if (groups.contains(cursor.getLong(index))) {
                    if (block) {
                        return false;
                    }
                }
            }

            return block ? true : false;
        } finally {
            cursor.close();
        }
    }

    public static boolean filterContact(final Context context, final Contact contact, final AnnouncifySettings settings) {
        if (!settings.getFilterByContact()) {
            return true;
        }

        final boolean block = settings.getBlockContacts();

        final ContactModel model = new ContactModel(context);
        final Cursor cursor = model.getResolver().query(model.buildUri(), new String[] { ContactModel.KEY_CONTACT_ID }, ContactModel.KEY_CONTACT_ID + " = ?", new String[] { contact.getLookupString() }, null);

        try {
            if (cursor.moveToFirst()) {
                return block ? false : true;
            } else {
                return block ? true : false;
            }
        } finally {
            cursor.close();
        }
    }
}
