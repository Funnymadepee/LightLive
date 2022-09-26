package com.lzm.lightLive.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DrawableCenterTextView : AppCompatTextView {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?) : super(context!!) {}

    override fun onDraw(canvas: Canvas) {
        // We want the icon and/or text grouped together and centered as a group.
        // We need to accommodate any existing padding
        val buttonContentWidth = (width - paddingLeft - paddingRight).toFloat()
        var textWidth = 0f
        val layout = layout
        if (layout != null) {
            for (i in 0 until layout.lineCount) {
                textWidth = Math.max(textWidth, layout.getLineRight(i))
            }
        }

        // Compute left drawable width, if any
        val drawables = compoundDrawables
        val drawableLeft = drawables[0]
        val drawableWidth = drawableLeft?.intrinsicWidth ?: 0

        // We only count the drawable padding if there is both an icon and text
        val drawablePadding =
            if (textWidth > 0 && drawableLeft != null) compoundDrawablePadding else 0

        // Adjust contents to center
        val bodyWidth = textWidth + drawableWidth + drawablePadding
        val translate = (buttonContentWidth - bodyWidth).toInt()
        if (translate != 0) setPadding(translate, 0, translate, 0)
        //        canvas.translate((buttonContentWidth - bodyWidth) / 2, 0);
        super.onDraw(canvas)
    }
}