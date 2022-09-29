package com.lzm.lib_base.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/*
* 生命周期:
*   完整的周期: onCreate - > onDestroy
*   可见: onStart - > onStop
*   前台周期: onResume - > onPause
* */
public class AppActivityStateWatcher {

    private static final String TAG = "AppActivityStateWatcher";

    private final Application application;
    public ActivityLifecycleCallbacks callbacks;

    private HandlerThread handlerThread;
    private Handler handler;

    public AppActivityStateWatcher(Context context) {
        this.application = (Application) context.getApplicationContext();
    }

    public void start() {
        handlerThread = new HandlerThread("app_state_thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        callbacks = new ActivityLifecycleCallbacks();
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    public void finish() {
        application.unregisterActivityLifecycleCallbacks(callbacks);
    }

    interface ActivityState {
        int onActivityCreated = 0;
        int onActivityStarted = 1;
        int onActivityResumed = 2;
        int onActivityPaused = 3;
        int onActivityStopped = 4;
        int onActivitySaveInstanceState = 5;
        int onActivityDestroyed = 6;
    }

    public static class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        private static final String TAG = "ActivityLifecycleCallbacks";

        /*visible list */
        /*front list */
        /*full life circle list */
        private final Map<String, ArrayList<Activity>> activityMap = new ConcurrentHashMap<>();

        private final Stack<Activity> activityStack = new Stack<>();

        public Activity getTopActivity() {
            return activityStack.peek();
        }

        private void RecordState(Activity activity, int state, String stateName) {
            long stateTime = System.currentTimeMillis();
            String actName = activity.getClass().getSimpleName();
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            RecordState(activity, ActivityState.onActivityCreated, "onActivityCreated");
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            RecordState(activity, ActivityState.onActivityStarted, "onActivityStarted");
            activityStack.push(activity);
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            RecordState(activity, ActivityState.onActivityResumed, "onActivityResumed");
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            RecordState(activity, ActivityState.onActivityPaused, "onActivityPaused");
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            RecordState(activity, ActivityState.onActivityStopped, "onActivityStopped");
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            RecordState(activity, ActivityState.onActivitySaveInstanceState, "onActivitySaveInstanceState");
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            RecordState(activity, ActivityState.onActivityDestroyed, "onActivityDestroyed");
        }
    }

}
