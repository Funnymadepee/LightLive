package com.lzm.lib_base.util;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.LinearSmoothScroller;

public class FasterLinearSmoothScroller extends LinearSmoothScroller {

    private int scrollType;
    public static final float DEFAULT_MILLISECONDS_PER_INCH = 80f;
    private float time = DEFAULT_MILLISECONDS_PER_INCH;

    @IntDef({SNAP_TO_ANY, SNAP_TO_START, SNAP_TO_END})
    public @interface ScrollType {
    }

    public FasterLinearSmoothScroller(Context context, @ScrollType int scrollType) {
        super(context);
        this.scrollType = scrollType;
    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return time / displayMetrics.densityDpi;
    }

    public void setTime(float milliseconds) {
        this.time = milliseconds;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return scrollType;
    }
}
