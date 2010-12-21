package com.announcify.contact;

import android.content.Context;

import com.announcify.util.AnnouncifySettings;

public class Contact {
	private final Context context;
	private final AnnouncifySettings settings;

	private String fullname = "";
	private String address = ""; // number, mail, twitter, ...
	private String nickname = "";
	private String addressType = "";
	private String lookupString = "";
	private int groupId = -1;

	public Contact(final Context context, final String address) {
		this.address = address;
		this.context = context;

		settings = new AnnouncifySettings(context);
	}

	/**
	 * 
	 * @return Returns true if you shouldn't announce this contact. Be sure to
	 *         really do so! Otherwise the user might get angry and throw birds
	 *         at your house!
	 */
	private boolean filter() {
		if (settings.getFilterByGroup() && !Filter.checkGroup(context, groupId)) {
			return true;
		}

		return false;
	}

	// TODO: lookupContact
	// guess addresstype -> lookup -> nickname

	public String getRawUserPreferredName() {
		if (filter()) {
			return "";
		}

		switch (settings.getReadingMode()) {
			case 0: // user requested to read full name
				return fullname;

			case 1: // user requested to read only first name
				if (fullname.contains(" ")) {
					return fullname.split(" ")[0];
				} else {
					return "";
				}

			case 2: // user requested to read only family name
				if (fullname.contains(" ")) {
					return fullname.split(" ")[1];
				} else {
					return "";
				}

			case 3: // user requested to read nickname (if available)
				return nickname;

			case 4: // user requested to read the number
				return address;

			default:
				return "";
		}
	}

	public String getUserPreferredName() {
		if (filter()) {
			return "";
		}

		switch (settings.getReadingMode()) {
			case 0: // user requested to read full name
				return getFullname();

			case 1: // user requested to read only first name
				return getFirstname();

			case 2: // user requested to read only family name
				return getLastname();

			case 3: // user requested to read nickname (if available)
				return getNickname();

			case 4: // user requested to read the number
				return getAddress();

			default:
				return "";
		}
	}

	public void setFullname(final String name) {
		if (name == null || name.trim().length() == 0) {
			return;
		}
		fullname = name;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(final String addressType) {
		if (addressType == null || addressType.trim().length() == 0) {
			return;
		}
		this.addressType = addressType;
	}

	public String getLookupString() {
		return lookupString;
	}

	public void setLookupString(final String lookupString) {
		if (lookupString == null || lookupString.trim().length() == 0) {
			return;
		}
		this.lookupString = lookupString;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(final int groupId) {
		if (groupId < 0) {
			return;
		}
		this.groupId = groupId;
	}

	public void setNickname(final String nickname) {
		if (nickname == null || nickname.trim().length() == 0) {
			return;
		}
		this.nickname = nickname;
	}

	public String getAddress() {
		// TODO: add address type
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public Context getContext() {
		return context;
	}

	public String getFullname() {
		if ("".equals(fullname)) {
			// plugin should decide wether to read the address, "unknown" or a
			// custom text
			return "";
		} else {
			return fullname;
		}
	}

	public String getNickname() {
		if ("".equals(nickname)) {
			return getFirstname();
		} else {
			return nickname;
		}
	}

	public String getFirstname() {
		if (fullname.contains(" ")) {
			return fullname.split(" ")[0];
		} else {
			return getFullname();
		}
	}

	public String getLastname() {
		if (fullname.contains(" ")) {
			return fullname.split(" ")[1];
		} else {
			return getFullname();
		}
	}
}