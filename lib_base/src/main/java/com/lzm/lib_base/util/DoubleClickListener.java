package com.lzm.lib_base.util;

import android.view.View;

public class DoubleClickListener implements View.OnClickListener{

    private final int clickCount;
    private final long duration;
    private final ClickEvent clickEvent;

    /**
     * @param clickCount 点击次数
     * @param duration 间隔时间
     * @param clickEvent 回调事件
     * */
    public DoubleClickListener(int clickCount, long duration, ClickEvent clickEvent) {
        this.clickCount = clickCount;
        this.duration = duration;
        this.clickEvent = clickEvent;
    }

    public interface ClickEvent {
        void onClick(View view);
    }

    private int count = 0;
    private long first = 0L;
    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (count == 0) {
            first = clickTime;
        }
        if (clickCount - 1 == count) {
            if (clickTime - first < duration) {
                count = 0;
                clickEvent.onClick(v);
            } else {
                count = 0;
            }
        }else
            count++;
    }
}
