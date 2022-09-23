package com.lzm.lib_base.status;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.lzm.lib_base.R;

@SuppressLint("ViewConstructor")
public class StatusBarHostLayout extends LinearLayout {

    private final Activity mActivity;
    private StatusBarView mStatusView;
    private FrameLayout mContentLayout;

    StatusBarHostLayout(Activity activity) {
        super(activity);
        this.mActivity = activity;
        //加载自定义的宿主布局
        if (mStatusView == null && mContentLayout == null) {
            setOrientation(LinearLayout.VERTICAL);

            mStatusView = new StatusBarView(mActivity);
            mStatusView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(mStatusView);

            mContentLayout = new FrameLayout(mActivity);
            mContentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0, 1.0f));
            addView(mContentLayout);
        }

        //替换宿主的contentView为外界设置的View
        replaceContentView();

        //设置原生的状态栏沉浸式，使用自定义的状态栏布局
        StatusBarHostUtils.immersiveStatusBar(mActivity);
        StatusBarHostUtils.setStatusBarColor(mActivity, Color.TRANSPARENT);
    }

    private void replaceContentView() {
        Window window = mActivity.getWindow();
        ViewGroup contentLayout = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        if (contentLayout.getChildCount() > 0) {
            //先找到DecorView的容器移除掉已经设置的ContentView
            View contentView = contentLayout.getChildAt(0);
            contentLayout.removeView(contentView);
            ViewGroup.LayoutParams contentParams = contentView.getLayoutParams();

            //外部设置的ContentView添加到宿主中来
            mContentLayout.addView(contentView, contentParams.width, contentParams.height);
        }
        //再把整个宿主添加到Activity对应的DecorView中去
        contentLayout.addView(this, -1, -1);
    }


    /**
     * 设置状态栏文本颜色为黑色
     */
    public StatusBarHostLayout setStatusBarBlackText() {
        StatusBarHostUtils.setStatusBarDarkFont(mActivity, true);
        return this;
    }

    /**
     * 设置状态栏文本颜色为白色
     */
    public StatusBarHostLayout setStatusBarWhiteText() {
        StatusBarHostUtils.setStatusBarDarkFont(mActivity, false);
        return this;
    }

    /**
     * 设置自定义状态栏布局的背景颜色
     */
    public StatusBarHostLayout setStatusBarBackground(int color) {
        mStatusView.setBackgroundColor(color);

        return this;
    }

    /**
     * 设置自定义状态栏布局的背景图片
     */
    public StatusBarHostLayout setStatusBarBackground(Drawable drawable) {

        mStatusView.setBackground(drawable);
        return this;
    }

    /**
     * 设置自定义状态栏布局的透明度
     */
    public StatusBarHostLayout setStatusBarBackgroundAlpha(int alpha) {
        Drawable background = mStatusView.getBackground();
        if (background != null) {
            background.mutate().setAlpha(alpha);
        }
        return this;
    }

    /**
     * 给指定的布局适配状态栏高度，设置paddingTop
     */
    public StatusBarHostLayout setViewFitsStatusBarView(View view) {

        //设置MarginTop的方式
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {

            //已经添加过了不要再次设置
            if (view.getTag() != null && view.getTag().equals("fitStatusBar")) {
                return this;
            }

            ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
            int marginTop = layoutParams.topMargin;
            int setMarginTop = marginTop + mStatusView.getStatusBarHeight();
            view.setTag("fitStatusBar");
            layoutParams.topMargin = setMarginTop;
            view.requestLayout();
        }

        return this;
    }


    /**
     * 设置自定义状态栏的沉浸式
     */
    public StatusBarHostLayout setStatusBarImmersive(boolean needImmersive) {
        layoutImmersive(needImmersive);
        return this;
    }

    //具体的沉浸式逻辑
    private void layoutImmersive(boolean needImmersive) {

        if (needImmersive) {
            mStatusView.setVisibility(GONE);
        } else {
            mStatusView.setVisibility(VISIBLE);
            mStatusView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.black));
        }

    }

}
