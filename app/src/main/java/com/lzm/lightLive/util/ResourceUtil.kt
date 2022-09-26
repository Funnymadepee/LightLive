package com.lzm.lightLive.util

import android.content.Context
import android.graphics.Color
import android.util.TypedValue

object ResourceUtil {
    fun attrColor(context: Context, attrColor: Int): Int {
        val attribute = intArrayOf(attrColor)
        val array = context.theme.obtainStyledAttributes(attribute)
        val color = array.getColor(0, Color.TRANSPARENT)
        array.recycle()
        return color
    }

    fun attrColorApplication(context: Context, attrRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(
                attrRes,
                typedValue,
                true
            )
        ) typedValue.data else Color.TRANSPARENT
    }
}