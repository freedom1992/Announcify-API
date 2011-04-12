package com.announcify.api.background.contact;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.announcify.api.background.contact.lookup.LookupMethod;

/**
 * Used to store information, such as name, number, etc about a contact.
 * 
 * @author tom
 */
public class Contact {

    private final LookupMethod lookupMethod;
    private final Context context;

    private String firstname = "";
    private String lastname = "";
    private String nickname = "";
    private String title = ""; // Dr., Sir, ...
    private String fullname = "";
    private String address = ""; // number, mail, twitter, ...
    private String type = "";
    private String lookupString = "";

    private LinkedList<Long> groups;

    public Contact() {
        lookupMethod = new LookupMethod() {

            public void getAddress() {
            }

            public void getLookup(final Contact contact) {
            }

            public void getType() {
            }
        };

        context = null;
    }

    public Contact(final Context context, final LookupMethod lookupMethod, String address) {
        this.address = address;
        this.context = context;
        this.lookupMethod = lookupMethod;

        if (address == null) {
            address = "";
        }

        if (!address.equals("")) {
            lookupMethod.getLookup(this);
        }
    }

    public String getAddress() {
        return address;
    }

    public String getAddressType() {
        return type;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getFullname() {
        return fullname;
    }

    public List<Long> getGroups() {
        return groups;
    }

    public String getLastname() {
        return lastname;
    }

    public LookupMethod getLookupMethod() {
        return lookupMethod;
    }

    public String getLookupString() {
        return lookupString;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUnknownString() {
        final String s = Resources.getSystem().getString(android.R.string.unknownName);
        return s.substring(1, s.length() - 1);
    }

    public void setAddress(final String address) {
        if ((address == null) || (address.trim().length() == 0)) {
            return;
        }
        this.address = address;
    }

    public void setAddressType(final String addressType) {
        if ((addressType == null) || (addressType.trim().length() == 0)) {
            return;
        }
        type = addressType;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public void setFullname(final String name) {
        if ((name == null) || (name.trim().length() == 0)) {
            return;
        }
        fullname = name;
    }

    public void setGroups(final LinkedList<Long> groups) {
        this.groups = groups;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public void setLookupString(final String lookupString) {
        if ((lookupString == null) || (lookupString.trim().length() == 0)) {
            return;
        }
        this.lookupString = lookupString;
    }

    public void setNickname(final String nickname) {
        if ((nickname == null) || (nickname.trim().length() == 0)) {
            return;
        }
        this.nickname = nickname;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
