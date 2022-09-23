package com.lzm.lightLive;

import android.app.Application;
import android.content.Context;
import com.kongzue.dialogx.DialogX;
import com.lzm.lightLive.util.SpUtils;

public class App extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        SpUtils.initSpUtil(getApplicationContext());
        DialogX.init(this);
    }

    public static Context getAppContext() {
        if (null == applicationContext) throw new RuntimeException("Should Use App.class in manifest rather than default Application");
        return applicationContext;
    }
}
