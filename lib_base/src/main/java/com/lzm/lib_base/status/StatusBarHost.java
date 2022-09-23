package com.lzm.lib_base.status;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class StatusBarHost {

    private StatusBarHost() { }

    public static StatusBarHostLayout inject(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup contentLayout = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        if (contentLayout.getChildCount() > 0) {
            View contentView = contentLayout.getChildAt(0);
            //如果当前是宿主的包装类，直接强转
            if (contentView instanceof StatusBarHostLayout) {
                return (StatusBarHostLayout) contentView;
            }
        }
        //如果不是我们封装一个宿主包装类
        return new StatusBarHostLayout(activity);
    }

}
