package com.announcify.api.background.contact.lookup;

import com.announcify.api.background.contact.Contact;

public interface LookupMethod {

    public void getAddress();

    public void getLookup(Contact contact);

    public void getType();
}
