package com.lzm.lightLive.view;

import android.content.Context;
import android.graphics.Color;
import com.lzm.lib_danmu.model.DanMuModel;
import com.lzm.lib_danmu.model.utils.DimensionUtil;

public class BasicDanMuModel extends DanMuModel {
    
    public BasicDanMuModel(Context context, String text) {

        setSpeed(200);
        setDisplayType(DanMuModel.RIGHT_TO_LEFT);
        setPriority(DanMuModel.NORMAL);
        marginLeft = DimensionUtil.dpToPx(context, 30);

        // 显示的文本内容
        textSize = DimensionUtil.spToPx(context, 14);
        textColor = Color.WHITE;
        textMarginLeft = DimensionUtil.dpToPx(context, 5);

        this.text = text;

        // 弹幕文本背景
        textBackgroundMarginLeft = DimensionUtil.dpToPx(context, 15);
        textBackgroundPaddingTop = DimensionUtil.dpToPx(context, 3);
        textBackgroundPaddingBottom = DimensionUtil.dpToPx(context, 3);
        textBackgroundPaddingRight = DimensionUtil.dpToPx(context, 15);
    }


}
