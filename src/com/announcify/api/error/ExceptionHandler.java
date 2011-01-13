
package com.announcify.api.error;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class ExceptionHandler implements UncaughtExceptionHandler {
    public static final String PACKAGE = "PACKAGE";

    public static final String ANDROID_VERSION = "ANDROID_VERSION";

    public static final String VERSION_CODE = "VERSION_CODE";

    public static final String VERSION_NAME = "VERSION_NAME";

    public static final String STACKTRACE = "STACKTRACE";

    public static final String MODEL = "MODEL";

    public static final String INFORMATION = "INFORMATION";

    private final Context context;

    private final UncaughtExceptionHandler defaultHandler;

    public ExceptionHandler(final Context context, final UncaughtExceptionHandler defaultHandler) {
        this.context = context;
        this.defaultHandler = defaultHandler;
    }

    public void uncaughtException(final Thread thread, final Throwable exception) {
        new ExceptionParser(context, exception).sendException();

        defaultHandler.uncaughtException(thread, exception);
    }
}
