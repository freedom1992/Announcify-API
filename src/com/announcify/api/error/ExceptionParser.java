
package com.announcify.api.error;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

public class ExceptionParser {
    private final Context context;

    private final Throwable exception;

    public ExceptionParser(final Context context, final Throwable exception) {
        this.context = context;
        this.exception = exception;
    }

    public void sendException() {
        final Intent exceptionIntent = new Intent("com.announcify.EXCEPTION");
        exceptionIntent.putExtra(ExceptionHandler.STACKTRACE, exception);
        exceptionIntent.putExtra(ExceptionHandler.PACKAGE, getPackageName());
        exceptionIntent.putExtra(ExceptionHandler.VERSION_CODE, getVersionCode());
        exceptionIntent.putExtra(ExceptionHandler.VERSION_NAME, getVersionName());

        context.sendBroadcast(exceptionIntent);
    }

    public String getPackageName() {
        return context.getPackageName();
    }

    public String getVersionCode() {
        try {
            return Integer.toString(context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode);
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
}
