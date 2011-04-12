package com.markupartist.android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author marco Workaround to be able to scroll text inside a TextView without
 *         it required to be focused. For some strange reason there isn't an
 *         easy way to do this natively.
 * 
 *         Original code written by Evan Cummings:
 *         http://androidbears.stellarpc.net/?p=185
 */
public class ScrollingTextView extends TextView {

    public ScrollingTextView(final Context context) {
        super(context);
    }

    public ScrollingTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollingTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(final boolean focused, final int direction, final Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(final boolean focused) {
        if (focused) {
            super.onWindowFocusChanged(focused);
        }
    }
}
