package com.lzm.lib_base.status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class StatusBarView extends View {

    private final int mBarSize;

    public StatusBarView(Context context) {
        this(context, null, 0);
    }

    public StatusBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBarSize = StatusBarHostUtils.getStatusBarHeight(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mBarSize);
    }

    //获取到当前的状态栏高度
    public int getStatusBarHeight() {
        return mBarSize;
    }
}
