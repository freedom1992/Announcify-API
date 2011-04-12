package com.announcify.api.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.announcify.api.R;

public class ActivityUtils {

    public static Intent getPluginsIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("http://plugins.announcify.com/"));
        // market://search?q=Announcify
    }

    public static Intent getHomeIntent() {
        final Intent intent = new Intent("com.announcify");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent getHelpIntent() {
        return new Intent("com.announcify.HELP");
    }

    public static Intent getShareIntent(final Context context) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.string_share_announcify));
        shareIntent.setType("text/plain");
        shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
        return shareIntent;
    }
}
