
package com.announcify.api.contact;

import android.content.Context;
import android.content.res.Resources;

import com.announcify.api.contact.lookup.LookupMethod;

/**
 * Used to store information, such as name, number, etc about a contact.
 * 
 * @author tom
 */
public class Contact {

    private final LookupMethod lookupMethod;

    private String firstname = "";

    private String lastname = "";

    private String nickname = "";

    private String title = ""; // Dr., Sir, ...

    private String fullname = "";

    private String address = ""; // number, mail, twitter, ...

    private String type = "";

    private String lookupString = "";

    private int groupId = -1;

    public Contact(final Context context, final LookupMethod lookup, final String address) {
        this.address = address;
        lookupMethod = lookup;
        lookupMethod.getLookup(this);
    }

    public void setFullname(final String name) {
        if (name == null || name.trim().length() == 0) {
            return;
        }
        fullname = name;
    }

    public void setAddressType(final String addressType) {
        if (addressType == null || addressType.trim().length() == 0) {
            return;
        }
        type = addressType;
    }

    public void setLookupString(final String lookupString) {
        if (lookupString == null || lookupString.trim().length() == 0) {
            return;
        }
        this.lookupString = lookupString;
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

    public void setAddress(final String address) {
        if (address == null || address.trim().length() == 0) {
            return;
        }
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAddressType() {
        return type;
    }

    public String getLookupString() {
        return lookupString;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getUnknownString() {
        return Resources.getSystem().getString(android.R.string.unknownName);
        // UNKNOWN = UNKNOWN.substring(1, UNKNOWN.length() - 1);
    }

    public LookupMethod getLookupMethod() {
        return lookupMethod;
    }
}
