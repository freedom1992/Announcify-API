
package com.announcify.api.contact.lookup;

import com.announcify.api.contact.Contact;

public interface LookupMethod {
    public void getLookup(Contact contact);

    public void getType();

    public void getAddress();
}
