package com.announcify.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * 
 * @author tom
 * 
 *         you can find a really good example of an IntentService here:
 *         http://github
 *         .com/commonsguy/cwac-wakeful/blob/master/src/com/commonsware
 *         /cwac/wakeful/WakefulIntentService.java
 * 
 *         so, we have to acquire a wake lock!
 */
public abstract class AnnouncifyService extends IntentService {
	public static final String ACTION_ANNOUNCE = "com.announcify.ANNOUNCE";

	public static final String EXTRA_PRIORITY = "com.announcify.EXTRA_PRIORITY";
	public static final String EXTRA_QUEUE = "com.announcify.EXTRA_QUEUE";

	public AnnouncifyService(final String name) {
		super(name);
	}

	@Override
	abstract protected void onHandleIntent(Intent intent);
}