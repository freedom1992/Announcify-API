package com.announcify.queue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;

import com.announcify.contact.Contact;
import com.announcify.text.Formatter;
import com.announcify.util.HeadsetFinder;
import com.announcify.util.PluginSettings;

public class PrepareMachine {
	public static final String MODE_CUSTOM = "com.announcify.CUSTOM";
	public static final String MODE_UNKNOWN = "com.announcify.UNKNOWN";

	private final PluginSettings settings;
	private final Context context;
	private final Contact contact;
	private String text;
	private final String message;

	public PrepareMachine(final Context context, final PluginSettings settings, final Contact contact, final String message) {
		this.settings = settings;
		this.context = context;
		this.contact = contact;
		this.message = message;
	}

	public LinkedList<Object> prepare() {
		final LinkedList<Object> list = new LinkedList<Object>();

		prepareName();

		if ("".equals(text) || text == null) {
			return list;
		}

		for (int i = 0; i < settings.getReadingRepeat(); i++) {
			list.add(text);
			list.add(new Integer(settings.getReadingBreak()));
		}

		return list;
	}

	private void prepareName() {
		final String temp = contact.getUserPreferredName();
		if ("".equals(temp)) {
			final String mode = settings.getUnknownMode();

			if ("".equals(mode)) {
				return;
			}

			if (MODE_UNKNOWN.equals(mode)) {
				text = getUnknownString();
				return;
			}

			applyFormat(getUnknownString());
		} else {
			text = temp;

			prepareAnnouncement(temp);
		}
	}
	
	private void prepareAnnouncement(final String name) {
		if (HeadsetFinder.isDiscreet(context)) {
			text = settings.getDiscreetMode();
		} else {
			text = settings.getAnnouncementMode();
		}

		applyFormat(name);
	}

	private void applyFormat(final String name) {
		final Map<String, String> expressions = new HashMap<String, String>();
		expressions.put(Formatter.ADDRESS, contact.getAddress());
		expressions.put(Formatter.ADDRESS_TYPE, contact.getAddressType());
		expressions.put(Formatter.NAME, name);
		expressions.put(Formatter.EVENT, settings.getEventType());
		expressions.put(Formatter.MESSAGE, message);

		text = Formatter.replaceExpressions(expressions, text);
	}

	private String getUnknownString() {
		return Resources.getSystem().getString(android.R.string.unknownName);
		// UNKNOWN = UNKNOWN.substring(1, UNKNOWN.length() - 1);
	}
}