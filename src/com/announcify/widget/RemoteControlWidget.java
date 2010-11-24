package com.announcify.widget;

import com.announcify.R;
import com.announcify.activity.RemoteControlDialog;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class RemoteControlWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews controlView = new RemoteViews(context.getPackageName(),R.layout.widget_control);
		Intent intent = new Intent(context, RemoteControlDialog.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		controlView.setOnClickPendingIntent(R.id.widget_control_button, PendingIntent.getActivity(context, 42, intent, 0));
		appWidgetManager.updateAppWidget(new ComponentName(context, RemoteControlWidget.class), controlView);
	}
}