package com.lzm.lightLive.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;

public class ResourceUtil {

    public static int attrColor(Context context, int attrColor) {
        int[] attribute = new int[] { attrColor };
        TypedArray array = context.getTheme().obtainStyledAttributes(attribute);
        int color = array.getColor(0, Color.TRANSPARENT);
        array.recycle();
        return color;
    }

    public static int attrColorApplication(Context context, int attrRes) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attrRes, typedValue, true))
            return typedValue.data;
        else
            return Color.TRANSPARENT;
    }
}
