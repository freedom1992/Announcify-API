package com.announcify.tts;

import java.util.Locale;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Speech implements Parcelable {
	public static final Creator<Speech> CREATOR = new Creator<Speech>() {
		public Speech createFromParcel(final Parcel source) {
			return new Speech(source);
		}

		public Speech[] newArray(final int size) {
			return new Speech[size];
		}
	};

	private final float pitch;
	private final float speechRate;
	private Locale language;

	public Speech(final float pitch, final float speechRate, final Locale language) {
		this.pitch = pitch;
		this.speechRate = speechRate;
		this.language = language;
	}

	public Speech(final float pitch, final float speechRate, final Context context) {
		this(pitch, speechRate, getDefaultLocale(context));
	}

	public Speech(final Context context) {
		this(getDefaultPitch(context), getDefaultSpeechRate(context), getDefaultLocale(context));
	}

	public Speech(final Locale language, final Context context) {
		this(getDefaultPitch(context), getDefaultSpeechRate(context), language);
	}

	public Speech(final Parcel source) {
		pitch = source.readFloat();
		speechRate = source.readFloat();
		language = new Locale(source.readString());
	}

	private static float getDefaultPitch(final Context context) {
		// try {
		// return Secure.getFloat(context.getContentResolver(),
		// Secure.TTS_DEFAULT_PITCH);
		// } catch (SettingNotFoundException e) {
		// e.printStackTrace();
		// }

		return 1;
	}

	private static float getDefaultSpeechRate(final Context context) {
		// try {
		// return Secure.getFloat(context.getContentResolver(),
		// Secure.TTS_DEFAULT_SYNTH);
		// } catch (SettingNotFoundException e) {
		// e.printStackTrace();
		// }

		return 1;
	}

	private static Locale getDefaultLocale(final Context context) {
		// return new Locale(Secure.getString(context.getContentResolver(),
		// Secure.TTS_DEFAULT_LANG));

		return Locale.getDefault();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeFloat(pitch);
		dest.writeFloat(speechRate);
		dest.writeString(language.getLanguage());
	}

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(final Locale language) {
		this.language = language;
	}

	public float getPitch() {
		return pitch;
	}

	public float getSpeechRate() {
		return speechRate;
	}
}