package com.lzm.lib_danmu.model.painter;

import android.graphics.Canvas;
import com.lzm.lib_danmu.model.DanMuModel;
import com.lzm.lib_danmu.model.channel.DanMuChannel;

abstract class IDanMuPainter {

    public abstract void execute(Canvas canvas, DanMuModel danMuView, DanMuChannel danMuChannel);

    public abstract void requestLayout();

    public abstract void setAlpha(int alpha);

    public abstract void hideNormal(boolean hide);

    public abstract void hideAll(boolean hideAll);

}