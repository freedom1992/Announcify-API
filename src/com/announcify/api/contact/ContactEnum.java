
package com.announcify.api.contact;

import android.content.Context;

import com.announcify.api.contact.lookup.Name;
import com.announcify.api.text.FormatEnum;
import com.announcify.api.text.Formatter;

public enum ContactEnum implements FormatEnum {
    UNKNOWN() {
        @Override
        public String getExpression() {
            return Formatter.UNKNOWN;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            return ((Contact)object).getUnknownString();
        }
    },

    FIRSTNAME() {
        @Override
        public String getExpression() {
            return Formatter.FIRSTNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            if ("".equals(((Contact)object).getFirstname())) {
                Name.getFirstname(context, ((Contact)object));
            }

            return ((Contact)object).getFirstname();
        }
    },

    LASTNAME() {
        @Override
        public String getExpression() {
            return Formatter.LASTNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            if ("".equals(((Contact)object).getLastname())) {
                Name.getLastname(context, ((Contact)object));
            }

            return ((Contact)object).getLastname();
        }
    },

    NICKNAME() {
        @Override
        public String getExpression() {
            return Formatter.NICKNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            if ("".equals(((Contact)object).getNickname())) {
                Name.getNickname(context, ((Contact)object));
            }

            if ("".equals(((Contact)object).getNickname())) {
                return FIRSTNAME.getSubstitution(context, object);
            } else {
                return ((Contact)object).getNickname();
            }
        }
    },

    TITLE() {
        @Override
        public String getExpression() {
            return Formatter.TITLE;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            if ("".equals(((Contact)object).getTitle())) {
                Name.getPrefix(context, ((Contact)object));
            }

            return ((Contact)object).getTitle();
        }
    },

    FULLNAME() {
        @Override
        public String getExpression() {
            return Formatter.FULLNAME;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            if ("".equals(((Contact)object).getFullname())) {
                Name.getFullname(context, ((Contact)object));
            }

            return ((Contact)object).getFullname();
        }
    },

    ADDRESS() {
        @Override
        public String getExpression() {
            return Formatter.ADDRESS;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            if (((Contact)object).getAddress().matches("[+][0-9]+")) {
                final StringBuilder builder = new StringBuilder();
                for (final char c : ((Contact)object).getAddress().toCharArray()) {
                    builder.append(c + ".");
                }
                return builder.toString();
            }

            return ((Contact)object).getAddress();
        }
    },

    ADDRESS_TYPE() {
        @Override
        public String getExpression() {
            return Formatter.ADDRESS_TYPE;
        }

        @Override
        public String getSubstitution(final Context context, final Object object) {
            if ("".equals(((Contact)object).getAddressType())) {
                ((Contact)object).getLookupMethod().getType();
            }

            return ((Contact)object).getAddressType();
        }
    };

    public abstract String getExpression();

    public abstract String getSubstitution(Context context, Object object);
}
