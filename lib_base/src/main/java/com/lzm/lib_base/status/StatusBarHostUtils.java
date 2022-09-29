package com.lzm.lib_base.status;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StatusBarHostUtils {

    /**
     * 设置当前页面的状态栏颜色，使用宿主方案一般不用这个修改颜色，只是用于沉浸式之后修改状态栏颜色为透明
     *
     * @param activity       指定页面
     * @param statusBarColor 状态栏颜色
     */
    public static void setStatusBarColor(Activity activity, int statusBarColor) {

        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setStatusBarColor(statusBarColor);

    }

    /**
     * 设置沉浸式状态
     *
     * @param activity 宿主activity
     */
    public static void immersiveStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }


    /**
     * 6.0版本及以上可以设置黑色的状态栏文本
     *
     * @param activity 宿主activity
     * @param darkFont 是否需要黑色文本
     * @return 是否修改成功
     */
    public static boolean setStatusBarDarkFont(Activity activity, boolean darkFont) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (darkFont) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return true;
    }


    /**
     * 获取状态栏高度
     *
     * @param context 宿主activity
     * @return 高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
