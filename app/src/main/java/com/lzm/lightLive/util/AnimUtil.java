package com.lzm.lightLive.util;

import android.animation.ObjectAnimator;
import android.view.View;

public class AnimUtil {

    public static void alphaAnim(View view, long duration, float... values) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", values);
        alpha.setDuration(duration);
        alpha.start();
    }

    public static void translationYAnim(View view, long duration, float... values) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY"
                , values);
        translationY.setDuration(duration);
        translationY.start();
    }
}
