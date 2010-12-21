package com.announcify.contact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;

public class Lookup {
	// TODO: lookup (name and nickname) and filter

	public static void lookupNumber(final Contact contact) {
		Cursor cursor = null;

		try {
			cursor = contact.getContext().getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contact.getAddress())), new String[] {PhoneLookup.DISPLAY_NAME, PhoneLookup.LABEL, PhoneLookup.TYPE, PhoneLookup.IN_VISIBLE_GROUP, PhoneLookup.LOOKUP_KEY}, null, null, null);

			final int nameIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			final int labelIndex = cursor.getColumnIndex(PhoneLookup.LABEL);
			final int typeIndex = cursor.getColumnIndex(PhoneLookup.TYPE);
			final int groupIndex = cursor.getColumnIndex(PhoneLookup.IN_VISIBLE_GROUP);
			final int lookupIndex = cursor.getColumnIndex(PhoneLookup.LOOKUP_KEY);

			if (cursor.moveToFirst()) {
				contact.setGroupId(cursor.getInt(groupIndex));

				contact.setFullname(cursor.getString(nameIndex));

				contact.setAddressType(cursor.getString(labelIndex));
				if (contact.getAddressType() != null) {
					contact.setAddressType(Resources.getSystem().getStringArray(android.R.array.phoneTypes)[Integer.parseInt(cursor.getString(typeIndex)) - 1]);
				}

				contact.setLookupString(cursor.getString(lookupIndex));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static void getNickname(final Contact contact) {
		if ("".equals(contact.getLookupString())) {
			return;
		}

		InputStream stream = null;
		try {
			final Uri uri = Uri.withAppendedPath(Contacts.CONTENT_VCARD_URI, Uri.encode(contact.getLookupString()));
			stream = contact.getContext().getContentResolver().openInputStream(uri);

			final int length = stream.available();
			if (length <= 0) {
				return;
			}

			final byte[] vcard = new byte[length];
			final int bytesRead = stream.read(vcard, 0, length);
			if (bytesRead < length) {
				return;
			}

			String vcardString = new String(vcard, 0, bytesRead, "UTF-8");

			final String SPLIT = "X-ANDROID-CUSTOM:vnd.android.cursor.item/nickname;";
			if (vcardString.contains(SPLIT)) {
				vcardString = vcardString.substring(vcardString.indexOf(SPLIT) + SPLIT.length());
				vcardString = vcardString.substring(0, vcardString.indexOf(';'));

				contact.setNickname(vcardString);
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (final IOException e) {}
			}
		}
	}

	public static void lookupMail(final Contact contact) {
		if (contact.getAddress().contains("\"")) {
			contact.setFullname(contact.getAddress().substring(1, contact.getAddress().lastIndexOf("\"")));
		}

		if (contact.getAddress().contains("<")) {
			contact.setAddress(contact.getAddress().substring(contact.getAddress().indexOf('<') + 1, contact.getAddress().indexOf('>')));
		}

		Cursor cursor = null;
		try {
			cursor = contact.getContext().getContentResolver().query(Email.CONTENT_URI, new String[] {Email.DISPLAY_NAME, Email.LABEL, Email.TYPE, Email.IN_VISIBLE_GROUP, Email.LOOKUP_KEY}, Email.DATA1 + "=" + "'" + contact.getAddress() + "'", null, null);

			final int nameIndex = cursor.getColumnIndex(Email.DISPLAY_NAME);
			final int labelIndex = cursor.getColumnIndex(Email.LABEL);
			final int typeIndex = cursor.getColumnIndex(Email.TYPE);
			final int groupIndex = cursor.getColumnIndex(Email.IN_VISIBLE_GROUP);
			final int lookupIndex = cursor.getColumnIndex(Email.LOOKUP_KEY);

			if (cursor.moveToFirst()) {
				contact.setGroupId(cursor.getInt(groupIndex));

				contact.setFullname(cursor.getString(nameIndex));

				contact.setAddressType(cursor.getString(labelIndex));
				if (contact.getAddressType() == null) {
					contact.setAddressType(Resources.getSystem().getStringArray(android.R.array.emailAddressTypes)[Integer.parseInt(cursor.getString(typeIndex)) - 1]);
				}

				contact.setLookupString(cursor.getString(lookupIndex));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}