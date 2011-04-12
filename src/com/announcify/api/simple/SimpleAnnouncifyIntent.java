package com.announcify.api.simple;

import android.content.Context;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.background.util.PluginSettings;

public class SimpleAnnouncifyIntent extends AnnouncifyIntent {

    @SuppressWarnings("unused")
    private class SimpleSettings extends PluginSettings {

        public SimpleSettings(final Context context) {
            super(context, "");
        }

        @Override
        public String getEventType() {
            return "";
        }

        @Override
        public int getPriority() {
            return 9;
        }

        @Override
        public int getReadingBreak() {
            return 0;
        }

        @Override
        public int getReadingRepeat() {
            return 1;
        }

        @Override
        public int getReadingWait() {
            return 0;
        }

        @Override
        public String getSettingsAction() {
            return "";
        }
    }

    public SimpleAnnouncifyIntent(final Context context) {
        super(context, null);
    }
}
