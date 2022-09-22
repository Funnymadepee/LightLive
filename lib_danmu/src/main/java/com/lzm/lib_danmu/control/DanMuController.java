package com.lzm.lib_danmu.control;

import android.graphics.Canvas;
import android.view.View;

import com.lzm.lib_danmu.control.dispatcher.DanMuDispatcher;
import com.lzm.lib_danmu.control.speed.RandomSpeedController;
import com.lzm.lib_danmu.control.speed.SpeedController;
import com.lzm.lib_danmu.model.DanMuModel;
import com.lzm.lib_danmu.model.channel.DanMuPoolManager;
import com.lzm.lib_danmu.model.painter.DanMuPainter;
import com.lzm.lib_danmu.view.IDanMuParent;

import java.util.List;

public final class DanMuController {

    private DanMuPoolManager danMuPoolManager;
    private DanMuDispatcher danMuRandomDispatcher;
    private SpeedController speedController;
    private boolean channelCreated = false;

    public DanMuController(View view) {
        if (speedController == null) {
            speedController = new RandomSpeedController();
        }
        if (danMuPoolManager == null) {
            danMuPoolManager = new DanMuPoolManager(view.getContext(), (IDanMuParent) view);
        }
        if (danMuRandomDispatcher == null) {
            danMuRandomDispatcher = new DanMuDispatcher(view.getContext());
        }
        danMuPoolManager.setDispatcher(danMuRandomDispatcher);
    }

    public void forceSleep() {
        danMuPoolManager.forceSleep();
    }

    public void forceWake() {
        if (danMuPoolManager != null) {
            danMuPoolManager.releaseForce();
        }
    }

    public void setSpeedController(SpeedController speedController) {
        if (speedController != null) {
            this.speedController = speedController;
        }
    }

    public void prepare() {
        danMuPoolManager.startEngine();
    }

    public void addDanMuView(int index, DanMuModel danMuView) {
        danMuPoolManager.addDanMuView(index, danMuView);
    }

    public void jumpQueue(List<DanMuModel> danMuViews) {
        danMuPoolManager.jumpQueue(danMuViews);
    }

    public void addPainter(DanMuPainter danMuPainter, int key) {
        danMuPoolManager.addPainter(danMuPainter, key);
    }

    public boolean isChannelCreated() {
        return channelCreated;
    }

    public void hide(boolean hide) {
        if (danMuPoolManager != null) {
            danMuPoolManager.hide(hide);
        }
    }

    public void hideAll(boolean hideAll) {
        if (danMuPoolManager != null) {
            danMuPoolManager.hideAll(hideAll);
        }
    }

    public void initChannels(Canvas canvas) {
        if (!channelCreated) {
            speedController.setWidthPixels(canvas.getWidth());
            danMuPoolManager.setSpeedController(speedController);
            danMuPoolManager.divide(canvas.getWidth(), canvas.getHeight());
            channelCreated = true;
        }
    }

    public void draw(Canvas canvas) {
        danMuPoolManager.drawDanMus(canvas);
    }

    public void release() {
        if (danMuPoolManager != null) {
            danMuPoolManager.release();
            danMuPoolManager = null;
        }
        if (danMuRandomDispatcher != null) {
            danMuRandomDispatcher.release();
        }
    }
}
