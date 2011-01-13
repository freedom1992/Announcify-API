
package com.announcify.api.sql.model;

import android.content.ContentValues;
import android.content.Context;

public class TranslationModel extends BaseModel {
    public final static String TABLE_NAME = "Translation";

    public static final String KEY_PLUGIN_FROM = "from_string";

    public static final String KEY_PLUGIN_TO = "to_string";

    public static final String KEY_PLUGIN_SOURCE = "source_language";

    public static final String KEY_PLUGIN_DESTINATION = "destination_language";

    public TranslationModel(final Context context) {
        super(context, TABLE_NAME);
    }

    public void add(final String from, final String to, final String source,
            final String destination) {
        final ContentValues values = new ContentValues();
        values.put(KEY_PLUGIN_FROM, from);
        values.put(KEY_PLUGIN_TO, to);
        values.put(KEY_PLUGIN_SOURCE, source);
        values.put(KEY_PLUGIN_DESTINATION, destination);
        database.insert(TABLE_NAME, null, values);
    }
}
