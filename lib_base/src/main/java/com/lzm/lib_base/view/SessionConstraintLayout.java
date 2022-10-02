package com.lzm.lib_base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SessionConstraintLayout extends ConstraintLayout {


    private DispatchKeyEventListener mDispatchKeyEventListener;

    public SessionConstraintLayout(Context context) {
        super(context);
    }

    public SessionConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SessionConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mDispatchKeyEventListener != null) {
            return mDispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return mDispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener mDispatchKeyEventListener) {
        this.mDispatchKeyEventListener = mDispatchKeyEventListener;
    }

}