package com.lzm.lib_base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 此类用于解决 ViewPager2  嵌套 ViewPager2 或者 RecyclerView 等相互嵌套的冲突问题，
 */
public class NestedScrollableHost extends FrameLayout {

    private int touchSlop;
    private float initialX = 0f;
    private float initialY = 0f;

    private ViewPager2 getParentViewPager() {
        ViewParent viewParent = this.getParent();
        if (!(viewParent instanceof View)) {
            viewParent = null;
        }

        View v;
        for (v = (View) viewParent; v != null && !(v instanceof ViewPager2); v = (View) viewParent) {
            viewParent = v.getParent();
            if (!(viewParent instanceof View)) {
                viewParent = null;
            }
        }

        View viewPager = v;
        if (!(v instanceof ViewPager2)) {
            viewPager = null;
        }

        return (ViewPager2) viewPager;
    }

    private View getChild() {
        return this.getChildCount() > 0 ? this.getChildAt(0) : null;
    }

    private boolean canChildScroll(int orientation, float delta) {
        int direction = -((int) Math.signum(delta));
        View viewChild;
        boolean canChildScroll = false;
        switch (orientation) {
            case 0:
                viewChild = this.getChild();
                canChildScroll = viewChild != null && viewChild.canScrollHorizontally(direction);
                break;
            case 1:
                viewChild = this.getChild();
                canChildScroll = viewChild != null && viewChild.canScrollVertically(direction);
                break;
            default:
//                 throw (Throwable)(new IllegalArgumentException());
        }
        return canChildScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        this.handleInterceptTouchEvent(e);
        return super.onInterceptTouchEvent(e);
    }

    private void handleInterceptTouchEvent(MotionEvent e) {
        ViewPager2 parentViewPager = this.getParentViewPager();
        if (parentViewPager != null) {
            int orientation = parentViewPager.getOrientation();
            if (this.canChildScroll(orientation, -1.0F)
                    || this.canChildScroll(orientation, 1.0F)) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        this.initialX = e.getX();
                        this.initialY = e.getY();
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = e.getX() - this.initialX;
                        float dy = e.getY() - this.initialY;
                        boolean isVpHorizontal = orientation == 0;
                        float scaledDx = Math.abs(dx) * (isVpHorizontal ? 0.5F : 1.0F);
                        float scaledDy = Math.abs(dy) * (isVpHorizontal ? 1.0F : 0.5F);
                        if (scaledDx > (float) this.touchSlop || scaledDy > (float) this.touchSlop) {
                            if (isVpHorizontal == scaledDy > scaledDx) {
                                this.getParent().requestDisallowInterceptTouchEvent(false);
                            } else {
                                this.getParent().requestDisallowInterceptTouchEvent(
                                        this.canChildScroll(orientation, isVpHorizontal ? dx : dy)
                                );

                            }
                        }
                        break;
                }

            }
        }
    }

    public NestedScrollableHost(Context context) {
        super(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.getContext());
        this.touchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public NestedScrollableHost(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.getContext());
        this.touchSlop = viewConfiguration.getScaledTouchSlop();
    }

}

