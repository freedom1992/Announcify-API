package com.announcify.tts;

import java.util.Locale;

public class SpeechSettings {
	public final static String UTTERANCE_KEY = "com.announcify.tts.utterance";

	public final boolean flush;

	public final double speed;

	public final int stream;

	public final Locale language;

	public SpeechSettings(final double speed, final int stream, final Locale language, final boolean flush) {
		this.flush = flush;
		this.speed = speed; // TODO: if -1; setToDefault
		this.stream = stream;
		this.language = language;
	}
}