package com.announcify.api.background.error;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class ExceptionParser {

    private final Context context;
    private final Throwable exception;

    public ExceptionParser(final Context context, final Throwable exception) {
        this.context = context.getApplicationContext();
        this.exception = exception;
    }

    public String getPackageName() {
        return context.getPackageName();
    }

    public String getVersionCode() {
        try {
            return Integer.toString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    public String getVersionName() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getAndroidVersion() {
        return Build.VERSION.SDK;
    }

    public void sendException() {
        final Intent exceptionIntent = new Intent("com.announcify.EXCEPTION");

        exceptionIntent.putExtra(ExceptionHandler.STACKTRACE, exception);
        exceptionIntent.putExtra(ExceptionHandler.PACKAGE, getPackageName());
        exceptionIntent.putExtra(ExceptionHandler.VERSION_CODE, getVersionCode());
        exceptionIntent.putExtra(ExceptionHandler.VERSION_NAME, getVersionName());
        exceptionIntent.putExtra(ExceptionHandler.MODEL, getModel());
        exceptionIntent.putExtra(ExceptionHandler.ANDROID_VERSION, getAndroidVersion());

        final PendingIntent pending = PendingIntent.getBroadcast(context, 18, exceptionIntent, 0);
        final AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pending);
    }
}