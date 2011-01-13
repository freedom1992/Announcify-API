
package com.announcify.api.contact;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class Filter {
    public static final String PREFERENCE_GROUP = "com.announcify.PREFERENCE_GROUP";

    /**
     * @return Returns true if you shouldn't announce this contact. Be sure to
     *         really do so! Otherwise the user might get angry and throw birds
     *         at your house!
     */
    // private boolean announcable() {
    // if (settings.getFilterByGroup() && !Filter.checkGroup(context, groupId))
    // {
    // return true;
    // }
    //
    // // TODO: check for null, empty, etc...
    //
    // return false;
    // }

    /**
     * @param context
     * @param groupId
     * @return Returns true if this contact isn't in user-desired group.
     */
    public static boolean checkGroup(final Context context, final int groupId) {
        try {
            if (context
                    .createPackageContext("com.announcify", 0)
                    .getSharedPreferences("org.mailboxer.saymyname.group",
                            Context.MODE_WORLD_READABLE).getAll().containsValue(groupId)) {
                return true;
            } else {
                return false;
            }
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}
