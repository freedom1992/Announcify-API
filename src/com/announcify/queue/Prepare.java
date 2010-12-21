package com.announcify.queue;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.announcify.contact.Contact;
import com.announcify.util.HeadsetFinder;
import com.announcify.util.PluginSettings;

public class Prepare {
	private final PluginSettings settings;
	private final Context context;
	private final Contact contact;
	private String text;
	private final String message;

	public Prepare(final Context context, final PluginSettings settings, final Contact contact, final String message) {
		this.settings = settings;
		this.context = context;
		this.contact = contact;
		this.message = message;
	}

	public String getName() {
		return text;
	}

	public void getQueue(final List<Object> list) {
		if (!prepareName()) {
			return;
		}
		prepareDiscreet();

		if ("".equals(text) || text == null) {
			return;
		}

		for (int i = 0; i < settings.getReadingRepeat(); i++) {
			list.add(text);
			list.add(new Integer(settings.getReadingBreak()));
		}
	}

	private boolean prepareName() {
		final String temp = contact.getUserPreferredName();
		if ("".equals(temp)) {
			switch (settings.getUnknownMode()) {
				case 0:
					// user requested not to speak if unknown
					text = "";
					return false;

				case 1:
					text = getUnknownString();
					break;

				case 2:
					text = getUnknownString();
					return false;

				case 3:
					final StringBuilder builder = new StringBuilder();
					for (final char c : contact.getAddress().toCharArray()) {
						builder.append(c + ".");
					}
					text = builder.toString();
					break;

				case 4:
					text = "You have a new " + settings.getEventType() + "!";
					return false;

				case 5:
					text = "You have a new Announcification!";
					return false;

				case 6:
					text = settings.getCustomAnnouncement();
					return false;

				default:
					text = "";
					return false;
			}
		} else {
			text = temp;
		}

		return true;
	}

	private void prepareDiscreet() {
		if (HeadsetFinder.isDiscreet(context)) {
			prepareDiscreetReading();
		} else {
			prepareReading();
		}
	}

	private void prepareReading() {
		switch (settings.getAnnouncementMode()) {
			case 0:
				break;

			case 1:
				text += " from " + contact.getAddressType();
				break;

			case 2:
				text = settings.getEventType() + " from " + text;
				break;

			case 3:
				text = settings.getEventType() + " from " + text + ": " + message;
				break;

			case 4:
				text = "You have a new " + settings.getEventType() + "!";
				break;

			case 5:
				text = "You have a new Announcification!";
				break;

			case 6:
				text = settings.getCustomAnnouncement();
				break;

			default:
				text = "";
				break;
		}
	}

	private void prepareDiscreetReading() {
		switch (settings.getDiscreetMode()) {
			case 0:
				prepareReading();

			default:
				text = "";
		}
	}

	private String getUnknownString() {
		return Resources.getSystem().getString(android.R.string.unknownName);
		// UNKNOWN = UNKNOWN.substring(1, UNKNOWN.length() - 1);
	}
}