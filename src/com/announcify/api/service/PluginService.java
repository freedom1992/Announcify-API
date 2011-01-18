
package com.announcify.api.service;

import android.app.IntentService;
import android.content.Intent;

public abstract class PluginService extends IntentService {
    public static final String ACTION_ANNOUNCE = "com.announcify.ANNOUNCE";

    public static final String EXTRA_PRIORITY = "com.announcify.EXTRA_PRIORITY";

    public static final String EXTRA_QUEUE = "com.announcify.EXTRA_QUEUE";

    public PluginService(final String name) {
        super(name);
    }

    @Override
    abstract protected void onHandleIntent(Intent intent);
}
