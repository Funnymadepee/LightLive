package com.lzm.lightLive;

import android.app.Application;

import com.lzm.lightLive.util.SpUtils;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SpUtils.getInstance(getApplicationContext());
    }
}
