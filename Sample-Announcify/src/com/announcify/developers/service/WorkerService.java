package com.announcify.developers.service;

import android.content.Intent;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.background.contact.Contact;
import com.announcify.api.background.contact.ContactFilter;
import com.announcify.api.background.service.PluginService;
import com.announcify.api.background.text.Formatter;
import com.announcify.developers.util.Settings;

public class WorkerService extends PluginService {

    public final static String ACTION_START_RINGTONE = "com.announcify.developers.ACTION_START_RINGTONE";
    public final static String ACTION_STOP_RINGTONE = "com.announcify.developers.ACTION_STOP_RINGTONE";

    public WorkerService() {
        super("Announcify - Developers", ACTION_START_RINGTONE, ACTION_STOP_RINGTONE);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (settings == null) {
            settings = new Settings(this);
        }

        if (ACTION_ANNOUNCE.equals(intent.getAction())) {
            // let's do the fun stuff now.
            final String message = intent.getStringExtra("com.announcify.developer.MESSAGE");
            String number = intent.getStringExtra("com.announcify.developer.NUMBER");
            if (number == null) {
                number = "";
            }

            final Settings settings = new Settings(this);
            final Contact contact = new Contact(this, new com.announcify.api.background.contact.lookup.Number(this), number);

            if (!settings.isChuckNorris()) {
                // i am chuck norris
                if (!ContactFilter.announcableContact(this, contact)) {
                    // not announcable? be nice and play a ringtone anyway...
                    playRingtone();
                    return;
                }
            }

            // fetching and mixing up the formatstring and needed informationes
            // about the contact
            final Formatter formatter = new Formatter(this, contact, settings);

            // let's announcify!
            final AnnouncifyIntent announcify = new AnnouncifyIntent(this, settings);
            // use this for starting a ringtone, for example
            announcify.setStartBroadcast(ACTION_START_RINGTONE);
            // use for stopping the the ringtone again
            announcify.setStopBroadcast(ACTION_STOP_RINGTONE);
            // be sure to call formatter.format(...)!
            announcify.announce(formatter.format(message));
        } else {
            // looks like we should either play / stop the ringtone, or register
            // ourselves in Announcify's database.
            super.onHandleIntent(intent);
        }
    }
}
