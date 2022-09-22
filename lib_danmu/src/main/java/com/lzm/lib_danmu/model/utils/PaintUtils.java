package com.lzm.lib_danmu.model.utils;

import android.graphics.Paint;
import android.text.TextPaint;

public class PaintUtils {

    private static TextPaint paint;

    public static TextPaint getPaint() {
        if(paint == null) {
            paint = new TextPaint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            paint.setStrokeWidth(3.5f);
        }
        return paint;
    }

}