package com.lzm.lightLive.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    private static SpUtils spUtils = null;

    private static final String TAG = "mSp";

    private SharedPreferences sharedPreferences = null;

    private SpUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public static synchronized SpUtils getInstance(Context context) {
        if(spUtils == null) {
            spUtils = new SpUtils(context);
        }
        return spUtils;
    }
}
