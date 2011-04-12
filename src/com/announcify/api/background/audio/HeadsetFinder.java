package com.announcify.api.background.audio;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HeadsetFinder {

    public static boolean detectHeadset(final Context context, final Intent intent) {
        if (intent == null) {
            return false;
        }

        if (intent.getIntExtra("state", 0) == 1) {
            return true;
        } else {
            return false;
        }

        // if (!intent.getExtras().containsKey("name")) {
        // return false;
        // }
        //
        // final String temp = intent.getStringExtra("name");
        // // if (temp != null && temp.toLowerCase().equals("headset")) {
        // if (temp != null && temp.trim().length() > 0) {
        // return true;
        // } else {
        // return false;
        // }
    }

    public static boolean isDiscreet(final Context context) {
        return detectHeadset(context, context.registerReceiver(null, new IntentFilter(Intent.ACTION_HEADSET_PLUG)));
    }
}
