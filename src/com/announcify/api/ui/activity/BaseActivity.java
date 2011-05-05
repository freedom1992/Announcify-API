package com.announcify.api.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.announcify.api.R;
import com.markupartist.android.widget.ActionBar;

public class BaseActivity extends Activity {

    protected void onCreate(final Bundle savedInstanceState, final int layoutResId) {
        setTheme(android.R.style.Theme_Light_NoTitleBar);

        super.onCreate(savedInstanceState);

        setContentView(layoutResId);

        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new ActionBar.IntentAction(this, ActivityUtils.getHomeIntent(), R.drawable.launcher_icon));

        actionBar.addAction(new ActionBar.IntentAction(this, ActivityUtils.getHelpIntent(), R.drawable.gd_action_bar_talk_normal));
        actionBar.addAction(new ActionBar.IntentAction(this, ActivityUtils.getPluginsIntent(), R.drawable.gd_action_bar_add_normal));
        actionBar.addAction(new ActionBar.IntentAction(this, ActivityUtils.getShareIntent(this), R.drawable.gd_action_bar_share_normal));
    }
}
