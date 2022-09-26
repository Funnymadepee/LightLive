package com.lzm.lightLive.view

import android.content.Context
import com.lzm.lib_danmu.model.DanMuModel
import com.lzm.lib_danmu.model.utils.DimensionUtil

class BasicDanMuModel(context: Context?, text: String?) : DanMuModel() {
    init {
        speed = 200f
        displayType = RIGHT_TO_LEFT
        priority = NORMAL
        marginLeft = DimensionUtil.dpToPx(context, 30)

        // 显示的文本内容
        textSize = DimensionUtil.spToPx(context, 14).toFloat()
        textMarginLeft = DimensionUtil.dpToPx(context, 5)
        this.text = text

        // 弹幕文本背景
        textBackgroundMarginLeft = DimensionUtil.dpToPx(context, 15)
        textBackgroundPaddingTop = DimensionUtil.dpToPx(context, 3)
        textBackgroundPaddingBottom = DimensionUtil.dpToPx(context, 3)
        textBackgroundPaddingRight = DimensionUtil.dpToPx(context, 15)
    }
}