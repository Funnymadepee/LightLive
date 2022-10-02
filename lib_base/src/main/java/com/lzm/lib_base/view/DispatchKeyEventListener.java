package com.lzm.lib_base.view;

import android.view.KeyEvent;

//监听接口
public interface DispatchKeyEventListener {
    boolean dispatchKeyEvent(KeyEvent event);
}