package com.announcify.api.background.tts;

import java.util.Locale;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings.Secure;

public class Speech implements Parcelable {

    public static final Creator<Speech> CREATOR = new Creator<Speech>() {

        public Speech createFromParcel(final Parcel source) {
            return new Speech(source);
        }

        public Speech[] newArray(final int size) {
            return new Speech[size];
        }
    };

    private static Locale getDefaultLocale(final Context context) {
        try {
            return new Locale(Secure.getString(context.getContentResolver(), Secure.TTS_DEFAULT_LANG));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return Locale.getDefault();
    }

    private static float getDefaultPitch(final Context context) {
        try {
            return Secure.getFloat(context.getContentResolver(), Secure.TTS_DEFAULT_PITCH);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    private static float getDefaultSpeechRate(final Context context) {
        try {
            return Secure.getFloat(context.getContentResolver(), Secure.TTS_DEFAULT_SYNTH);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    private final float pitch;
    private final float speechRate;

    private Locale language;

    public Speech(final Context context) {
        this(getDefaultPitch(context), getDefaultSpeechRate(context), getDefaultLocale(context));
    }

    public Speech(final float pitch, final float speechRate, final Context context) {
        this(pitch, speechRate, getDefaultLocale(context));
    }

    public Speech(final float pitch, final float speechRate, final Locale language) {
        this.pitch = pitch;
        this.speechRate = speechRate;
        this.language = language;
    }

    public Speech(final Locale language, final Context context) {
        this(getDefaultPitch(context), getDefaultSpeechRate(context), language);
    }

    public Speech(final Parcel source) {
        pitch = source.readFloat();
        speechRate = source.readFloat();
        language = new Locale(source.readString());
    }

    public int describeContents() {
        return 0;
    }

    public Locale getLanguage() {
        return language;
    }

    public float getPitch() {
        return pitch;
    }

    public float getSpeechRate() {
        return speechRate;
    }

    public void setLanguage(final Locale language) {
        this.language = language;
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeFloat(pitch);
        dest.writeFloat(speechRate);
        dest.writeString(language.getLanguage());
    }
}