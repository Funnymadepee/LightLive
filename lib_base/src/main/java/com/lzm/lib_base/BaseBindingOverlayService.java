package com.lzm.lib_base;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseBindingOverlayService<B extends IBinder, VB extends ViewDataBinding> extends Service {

    protected WindowManager windowManager;
    protected WindowManager.LayoutParams layoutParams;
    protected VB mBind;
    protected int startId;

    protected boolean touchMovable = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getBinder();
    }

    public abstract B getBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        initFloatingWindow(intent.getExtras());
        return super.onStartCommand(intent, flags, startId);
    }

    protected abstract @LayoutRes int getLayoutId();

    private void initFloatingWindow(Bundle bundleExtra) {
        if (Settings.canDrawOverlays(this)) {
            // 获取WindowManager服务
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            // 设置LayoutParam
            layoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //宽高自适应
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //显示的位置
            layoutParams.x = 0;
            layoutParams.y = 0;

            mBind = DataBindingUtil.inflate(
                    LayoutInflater.from(this),
                    getLayoutId(),
                    null,
                    false );

            mBind.getRoot().setOnTouchListener(new FloatingOnTouchListener());
            // 将悬浮窗控件添加到WindowManager
            windowManager.addView(mBind.getRoot(), layoutParams);
            iniView(bundleExtra);
        }else {
            canNotDrawOverlays();
        }
    }

    protected abstract void iniView(Bundle bundleExtra);

    protected abstract void canNotDrawOverlays();


    public class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        private boolean moved = false;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (!touchMovable) return false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    moved = false;
                    return false;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    moved = true;
                    return false;
                case MotionEvent.ACTION_UP:
                    return moved;
                default:
                    return false;
            }
        }
    }

    //Click Perform Runnable.
    /*private final class PerformClick implements Runnable {
        @Override
        public void run() {
            mBind.getRoot().performClick();
        }
    }*/

    public void updateViewLayout(View view, WindowManager.LayoutParams layoutParams) {
        windowManager.updateViewLayout(view, layoutParams);
    }

    public boolean isTouchMovable() {
        return touchMovable;
    }

    public void setTouchMovable(boolean touchMovable) {
        this.touchMovable = touchMovable;
    }

}
