package com.lzm.lib_base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.annotation.LayoutRes;

public class NotifyDialog extends Dialog implements View.OnTouchListener{

    protected View mView;
    private int downX = 0;
    private final int mScreenWidth;
    private long downTime = 0;
    private int downFirst = 0;
    private int speedThreshold = 1500;
    private long duration = 5000;
    private boolean isShow = false;
    private boolean touchMovable = true;
    private boolean autoDismiss = true;
    private boolean scrollDismissAble = true;
    private WindowManager.LayoutParams mParams;
    private static ValueAnimator restoreAnimator;
    private static ValueAnimator dismissAnimator;



    public NotifyDialog(Context context, @LayoutRes int resId, int gravity) {
        super(context);
        mView = View.inflate(context, resId, null);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        setContentView(mView);
        setParams();
        mView.setOnTouchListener(this);
        final Window window = getWindow();
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setContentView(mView);
        window.setGravity(gravity);
        window.setLayout(getWindow().getContext().getResources().getDisplayMetrics().widthPixels,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setDuration(long duration){
        this.duration = duration;
    }

    public void setSpeedThreshold(int speedThreshold){
        this.speedThreshold = speedThreshold;
    }

    public void setTouchMovable(boolean touchMovable){
        this.touchMovable = touchMovable;
    }
    public void setAutoDismiss(boolean autoDismiss){
        this.autoDismiss = autoDismiss;
    }
    public void setScrollDismissAble(boolean scrollDismissAble){
        this.scrollDismissAble = scrollDismissAble;
    }


    @Override
    public void show() {
        super.show();
        isShow = true;
        if(autoDismiss){
            new CountDownTimer(duration, 1000) {
                @Override
                public void onTick(long millisUntilFinished) { }
                @Override
                public void onFinish() {
                    if(isShow){
                        dismiss();
                    }
                }
            }.start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    public <T extends View> T findById(int resId){
        return mView.findViewById(resId);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(touchMovable){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = (int) event.getRawX();
                    downFirst = downX;
                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) (event.getRawX() - downX);
                    updatePosition(mParams.x + moveX, 0);
                    downX = (int) event.getRawX();
                    break;
                case MotionEvent.ACTION_UP:
                    long period = System.currentTimeMillis() - downTime;
                    int speed = (int) ((downX - downFirst) * 1000 / period);
                    dismissOrRestore(speed);
                    break;
            }
            return true;
        }
        return false;
    }

    private void dismissOrRestore(int speed) {
        boolean needDismiss = true;
        boolean toLeft = false;
        System.err.println(speed);
        if(Math.abs(speed) < speedThreshold) {
            if (mParams.x >= -(mScreenWidth / 2) && mParams.x <= (mScreenWidth / 2)) {
                needDismiss = false;
            }
        }else {
            if(mParams.x < 0){
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

    private boolean updatePosition(int x, int y) {
        if (isShow) {
            mParams.x = x;
            mParams.y = y;
            if(x > 0){
                getWindow().getDecorView().setPadding(x, y, 0, 0);
            }else {
                getWindow().getDecorView().setPadding(0, y, -x, 0);
            }
            float alpha = (float) (1.0 - 0.5 * Math.abs(mParams.x) * 2.0 / mScreenWidth);
            mView.setAlpha(alpha);
            return true;
        }
        return false;
    }

    private void setParams() {
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.width = mScreenWidth;
        mParams.y = 0;
    }

    private void createRestoreAnimator() {
        if (restoreAnimator == null) {
            restoreAnimator = ObjectAnimator.ofInt(mParams.x, 0);
            int duration = (int) (250*Math.abs(mParams.x)*2.0/mScreenWidth);
            restoreAnimator.setDuration(duration);
            restoreAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                updatePosition(value, 0);
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
                dismissAnimator = ObjectAnimator.ofInt(mParams.x, -mScreenWidth);
            }else {
                dismissAnimator = ObjectAnimator.ofInt(mParams.x, mScreenWidth);
            }
            dismissAnimator.setDuration(200);
            dismissAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                updatePosition(value,0);
            });
            dismissAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dismiss();
                    dismissAnimator=null;
                }
            });
        }
    }

}
