
package com.announcify.api.contact;

import android.content.Context;

import com.announcify.api.contact.lookup.Name;

public enum ContactEnum {
    UNKNOWN() {
        @Override
        public String getExpression() {
            return Formatter.UNKNOWN;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            return contact.getUnknownString();
        }
    },

    FIRSTNAME() {
        @Override
        public String getExpression() {
            return Formatter.FIRSTNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            if ("".equals(contact.getFirstname())) {
                Name.getFirstname(context, contact);
            }

            return contact.getFirstname();
        }
    },

    LASTNAME() {
        @Override
        public String getExpression() {
            return Formatter.LASTNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            if ("".equals(contact.getLastname())) {
                Name.getLastname(context, contact);
            }

            return contact.getLastname();
        }
    },

    NICKNAME() {
        @Override
        public String getExpression() {
            return Formatter.NICKNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            if ("".equals(contact.getNickname())) {
                Name.getNickname(context, contact);
            }

            return contact.getNickname();
        }
    },

    TITLE() {
        @Override
        public String getExpression() {
            return Formatter.TITLE;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            if ("".equals(contact.getTitle())) {
                Name.getPrefix(context, contact);
            }

            return contact.getTitle();
        }
    },

    FULLNAME() {
        @Override
        public String getExpression() {
            return Formatter.FULLNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            if ("".equals(contact.getFullname())) {
                Name.getFullname(context, contact);
            }

            return contact.getFullname();
        }
    },

    ADDRESS() {
        @Override
        public String getExpression() {
            return Formatter.ADDRESS;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            if (contact.getAddress().matches("[+][0-9]+")) {
                final StringBuilder builder = new StringBuilder();
                for (final char c : contact.getAddress().toCharArray()) {
                    builder.append(c + ".");
                }
                return builder.toString();
            }

            return contact.getAddress();
        }
    },

    ADDRESS_TYPE() {
        @Override
        public String getExpression() {
            return Formatter.ADDRESS_TYPE;
        }

        @Override
        public String getSubstitution(final Context context, final Contact contact) {
            if ("".equals(contact.getAddressType())) {
                contact.getLookupMethod().getType();
            }

            return contact.getAddressType();
        }
    };

    public abstract String getExpression();

    public abstract String getSubstitution(Context context, Contact contact);
}
