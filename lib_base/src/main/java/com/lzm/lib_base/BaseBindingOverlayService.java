package com.lzm.lib_base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
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
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.ViewModel;

public abstract class BaseBindingOverlayService<B extends IBinder, VB extends ViewDataBinding, VM extends ViewModel>
        extends LifecycleService {

    protected WindowManager windowManager;
    protected WindowManager.LayoutParams layoutParams;
    protected VB mBind;
    protected VM viewModel;
    protected int startId;
    protected boolean touchMovable = true;
    private int speedThreshold = 1500;
    private int mScreenWidth;
    private boolean scrollDismissAble = false;
    private static ValueAnimator restoreAnimator;
    private static ValueAnimator dismissAnimator;
    protected LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return getBinder();
    }

    public abstract B getBinder();

    @Override
    public void onCreate() {
        initFloatingWindow();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        return super.onStartCommand(intent, flags, startId);
    }

    protected abstract VM getViewModel();
    protected abstract void setViewModel(VM viewModel);
    protected abstract @LayoutRes int getLayoutId();

    public abstract LayoutInflater getLayoutInflater();

    public void initFloatingWindow() {
        // 获取WindowManager服务
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 设置LayoutParam
        layoutParams = new WindowManager.LayoutParams();
        if (Settings.canDrawOverlays(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
        }else {

        }

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags
                =
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //宽高自适应
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //显示的位置
        layoutParams.x = 0;
        layoutParams.y = 0;

        mBind = DataBindingUtil.inflate(
                getLayoutInflater(),
                getLayoutId(),
                null,
                false );

        mBind.setLifecycleOwner(this);
        setViewModel(getViewModel());
        mBind.getRoot().setOnTouchListener(new FloatingOnTouchListener());
        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(mBind.getRoot(), layoutParams);

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    protected abstract void canNotDrawOverlays();


    public class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        private boolean moved = false;
        private long downTime = 0L;
        private int downX = 0;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (!touchMovable) return false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    downX = x;
                    moved = false;
                    downTime = System.currentTimeMillis();
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
//                    windowManager.updateViewLayout(view, layoutParams);
                    moved = true;
                    return false;
                case MotionEvent.ACTION_UP:
                    long period = System.currentTimeMillis() - downTime;
                    int speed = (int) ((x - downX) * 1000 / period);
                    dismissOrRestore(speed);
                    return moved;
                default:
                    return false;
            }
        }
    }

    private void dismissOrRestore(int speed) {
        boolean needDismiss = true;
        boolean toLeft = false;
        if(Math.abs(speed) < speedThreshold) {
            if (layoutParams.x >= -(mScreenWidth / 2) && layoutParams.x <= (mScreenWidth / 2)) {
                needDismiss = false;
            }
        }else {
            if(layoutParams.x < 0){
                toLeft = true;
            }
        }
        if (needDismiss && scrollDismissAble) {
            createDismissAnimator(toLeft);
            dismissAnimator.start();
        }else{
            createRestoreAnimator();
            restoreAnimator.start();
        }
    }

    public void updateViewLayout(View view, WindowManager.LayoutParams layoutParams) {
        windowManager.updateViewLayout(view, layoutParams);
    }

    public boolean isTouchMovable() {
        return touchMovable;
    }

    public void setTouchMovable(boolean touchMovable) {
        this.touchMovable = touchMovable;
    }

    private void createRestoreAnimator() {
        if (restoreAnimator == null) {
            restoreAnimator = ObjectAnimator.ofInt(layoutParams.x, 0);
            int duration = (int) (250*Math.abs(layoutParams.x) * 2.0 / mScreenWidth);
            restoreAnimator.setDuration(duration);
            restoreAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
//                windowManager.updatePosition(value, 0);
            });
            restoreAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    restoreAnimator = null;
                }
            });
        }
    }

    private void createDismissAnimator(boolean isToLeft){
        if (dismissAnimator == null) {
            if(isToLeft){
                dismissAnimator = ObjectAnimator.ofInt(layoutParams.x, -mScreenWidth);
            }else {
                dismissAnimator = ObjectAnimator.ofInt(layoutParams.x, mScreenWidth);
            }
            dismissAnimator.setDuration(200);
            dismissAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
//                updatePosition(value,0);
            });
            dismissAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
//                    dismiss();
                    dismissAnimator=null;
                }
            });
        }
    }

}
