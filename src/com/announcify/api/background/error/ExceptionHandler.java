package com.announcify.api.background.error;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.util.Log;

public class ExceptionHandler implements UncaughtExceptionHandler {

    public static final String PACKAGE = "package";
    public static final String ANDROID_VERSION = "android";
    public static final String VERSION_CODE = "code";
    public static final String VERSION_NAME = "name";
    public static final String STACKTRACE = "stacktrace";
    public static final String MODEL = "model";

    private final Context context;

    // private final UncaughtExceptionHandler defaultHandler;

    public ExceptionHandler(final Context context) {
        this.context = context;
        // this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(final Thread thread, final Throwable exception) {
        Log.e("JOPPFM", "Reporting Exception", exception);

        new ExceptionParser(context, exception).sendException();

        // defaultHandler.uncaughtException(thread, exception);

        System.exit(1);
    }
}
