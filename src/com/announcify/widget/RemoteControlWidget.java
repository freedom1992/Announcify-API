package com.announcify.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.announcify.R;
import com.announcify.activity.RemoteControlDialog;

public class RemoteControlWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		final RemoteViews controlView = new RemoteViews(context.getPackageName(), R.layout.widget_control);
		final Intent intent = new Intent(context, RemoteControlDialog.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		controlView.setOnClickPendingIntent(R.id.widget_control_button, PendingIntent.getActivity(context, 42, intent, 0));
		appWidgetManager.updateAppWidget(new ComponentName(context, RemoteControlWidget.class), controlView);
	}
}