package com.lzm.lib_danmu.model.painter;

import com.lzm.lib_danmu.model.DanMuModel;
import com.lzm.lib_danmu.model.channel.DanMuChannel;

public class R2LPainter extends DanMuPainter {

    @Override
    protected void layout(DanMuModel danMuView, DanMuChannel danMuChannel) {
        if (danMuView.getX() - danMuView.getSpeed() <= -danMuView.getWidth()) {
            danMuView.setAlive(false);
            return;
        }
        danMuView.setStartPositionX(danMuView.getX() - danMuView.getSpeed());
    }
}