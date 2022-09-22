package com.lzm.lib_danmu.model.channel;

import com.lzm.lib_danmu.control.dispatcher.IDanMuDispatcher;
import com.lzm.lib_danmu.control.speed.SpeedController;
import com.lzm.lib_danmu.model.DanMuModel;

import java.util.List;

interface IDanMuPoolManager {
    void setSpeedController(SpeedController speedController);

    void addDanMuView(int index, DanMuModel iDanMuView);

    void jumpQueue(List<DanMuModel> danMuViews);

    void divide(int width, int height);

    void setDispatcher(IDanMuDispatcher iDanMuDispatcher);

    void hide(boolean hide);

    void hideAll(boolean hideAll);

    void startEngine();
}