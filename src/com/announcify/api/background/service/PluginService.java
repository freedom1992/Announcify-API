package com.announcify.api.background.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.sql.model.PluginModel;
import com.announcify.api.background.util.AnnouncifySettings;
import com.announcify.api.background.util.PluginSettings;

public abstract class PluginService extends IntentService {

    public static final String ACTION_PLUGIN_CONTACT = "com.announcify.ACTION_PLUGIN_CONTACT";
    public static final String ACTION_ANNOUNCE = "com.announcify.ANNOUNCE";
    public static final String EXTRA_PRIORITY = "com.announcify.EXTRA_PRIORITY";
    public static final String EXTRA_QUEUE = "com.announcify.EXTRA_QUEUE";
    public static final String EXTRA_PLUGIN_NAME = "com.announcify.EXTRA_PLUGIN_NAME";
    public static final String EXTRA_PLUGIN_PACKAGE = "com.announcify.EXTRA_PLUGIN_PACKAGE";
    public static final String EXTRA_PLUGIN_ACTION = "com.announcify.EXTRA_PLUGIN_ACTION";
    public static final String EXTRA_PLUGIN_BROADCAST = "com.announcify.EXTRA_PLUGIN_BROADCAST";
    public static final String EXTRA_PLUGIN_PRIORITY = "com.announcify.EXTRA_PLUGIN_PRIORITY";

    protected static Ringtone ringtone;

    public final String ACTION_START_RINGTONE;
    public final String ACTION_STOP_RINGTONE;

    protected PluginSettings settings;

    public PluginService(final String name) {
        super(name);

        ACTION_START_RINGTONE = "";
        ACTION_STOP_RINGTONE = "";
    }

    public PluginService(final String name, final String startRingtoneAction, final String stopRingtoneAction) {
        super(name);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));

        ACTION_START_RINGTONE = startRingtoneAction;
        ACTION_STOP_RINGTONE = stopRingtoneAction;
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (ACTION_PLUGIN_CONTACT.equals(intent.getAction())) {
            final PluginModel model = new PluginModel(this);

            final long id = model.getId(settings.getEventType());
            if (id < 0) {
                model.add(settings.getEventType(), settings.getPriority(), settings.getSettingsAction(), getPackageName(), false);
            } else {
                model.update(id, settings.getEventType(), settings.getPriority(), settings.getSettingsAction(), getPackageName(), false);
            }
        } else if (ACTION_START_RINGTONE.equals(intent.getAction())) {
            playRingtone();
        } else if (ACTION_STOP_RINGTONE.equals(intent.getAction())) {
            stopRingtone();
        }
    }

    protected void playRingtone() {
        final String s = settings.getRingtone();
        if ((s == null) || "".equals(s)) {
            return;
        }

        final RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALL);
        ringtone = manager.getRingtone(manager.getRingtonePosition(Uri.parse(s)));
        if (ringtone == null) {
            return;
        }

        // TODO: AnnouncifySettings using ContentProvider
        ringtone.setStreamType(new AnnouncifySettings(this).getStream());
        ringtone.play();
    }

    protected void stopRingtone() {
        if (ringtone != null) {
            ringtone.stop();
            ringtone = null;
        }
    }
}
