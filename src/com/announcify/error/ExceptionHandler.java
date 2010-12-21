package com.announcify.error;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

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
		final Intent exceptionIntent = new Intent("com.announcify.EXCEPTION");
		exceptionIntent.putExtra(STACKTRACE, exception);
		exceptionIntent.putExtra(PACKAGE, getPackageName());
		exceptionIntent.putExtra(VERSION_CODE, getVersionCode());
		exceptionIntent.putExtra(VERSION_NAME, getVersionName());

		context.sendBroadcast(exceptionIntent);

		defaultHandler.uncaughtException(thread, exception);
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
}