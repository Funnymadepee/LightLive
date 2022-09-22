package com.lzm.lib_base.behavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.lzm.lib_base.R;

public class SimpleViewBehavior extends CoordinatorLayout.Behavior<View> {

    private static final int UNSPECIFIED_INT = Integer.MAX_VALUE;
    private static final float UNSPECIFIED_FLOAT = Float.MAX_VALUE;

    private static final int DEPEND_TYPE_HEIGHT = 0;

    private static final int DEPEND_TYPE_WIDTH = 1;

    private static final int DEPEND_TYPE_X = 2;

    private static final int DEPEND_TYPE_Y = 3;

    private int mDependType;
    private int mDependViewId;

    private int mDependTargetX;
    private int mDependTargetY;
    private int mDependTargetWidth;
    private int mDependTargetHeight;

    private int mDependStartX;
    private int mDependStartY;
    private int mDependStartWidth;
    private int mDependStartHeight;

    private int mStartX;
    private int mStartY;
    private int mStartWidth;
    private int mStartHeight;
    private int mStartBackgroundColor;
    private float mStartAlpha;
    private float mStartRotateX;
    private float mStartRotateY;

    private int mTargetX;
    private int mTargetY;
    private int mTargetWidth;
    private int mTargetHeight;
    private int mTargetBackgroundColor;
    private float mTargetAlpha;
    private float mTargetRotateX;
    private float mTargetRotateY;

    private int mAnimationId;
    private Animation mAnimation;

    private boolean isPrepared;

    private Context mContext;

    private float scale;

    public SimpleViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        scale = mContext.getResources().getDisplayMetrics().density;
        // setting values
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleBehavior);
        mDependViewId = a.getResourceId(R.styleable.SimpleBehavior_dependsOn, 0);
        mDependType = a.getInt(R.styleable.SimpleBehavior_dependType, DEPEND_TYPE_WIDTH);
        mDependTargetX = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_dependTargetX, UNSPECIFIED_INT);
        mDependTargetY = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_dependTargetY, UNSPECIFIED_INT);
        mDependTargetWidth = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_dependTargetWidth, UNSPECIFIED_INT);
        mDependTargetHeight = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_dependTargetHeight, UNSPECIFIED_INT);
        mTargetX = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_targetX, UNSPECIFIED_INT);
        mTargetY = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_targetY, UNSPECIFIED_INT);
        mTargetWidth = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_targetWidth, UNSPECIFIED_INT);
        mTargetHeight = a.getDimensionPixelOffset(R.styleable.SimpleBehavior_targetHeight, UNSPECIFIED_INT);
        mTargetBackgroundColor = a.getColor(R.styleable.SimpleBehavior_targetBackgroundColor, UNSPECIFIED_INT);
        mTargetAlpha = a.getFloat(R.styleable.SimpleBehavior_targetAlpha, UNSPECIFIED_FLOAT);
        mTargetRotateX = a.getFloat(R.styleable.SimpleBehavior_targetRotateX, UNSPECIFIED_FLOAT);
        mTargetRotateY = a.getFloat(R.styleable.SimpleBehavior_targetRotateY, UNSPECIFIED_FLOAT);
        mAnimationId = a.getResourceId(R.styleable.SimpleBehavior_animation, 0);
        a.recycle();
    }

    private void prepare(CoordinatorLayout parent, View child, View dependency) {
        mDependStartX = (int) dependency.getX();
        mDependStartY = (int) dependency.getY();
        mDependStartWidth = dependency.getWidth();
        mDependStartHeight = dependency.getHeight();
        mStartX = (int) child.getX();
        mStartY = (int) child.getY();
        mStartWidth = child.getWidth();
        mStartHeight = child.getHeight();
        mStartAlpha = child.getAlpha();
        mStartRotateX = child.getRotationX();
        mStartRotateY = child.getRotationY();
        if (child.getBackground() instanceof ColorDrawable) {
            mStartBackgroundColor = ((ColorDrawable) child.getBackground()).getColor();
        }

        if (mAnimationId != 0) {
            mAnimation = AnimationUtils.loadAnimation(child.getContext(), mAnimationId);
            mAnimation.initialize(child.getWidth(), child.getHeight(), parent.getWidth(), parent.getHeight());
        }

        if (Build.VERSION.SDK_INT > 16 && parent.getFitsSystemWindows() && mTargetY != UNSPECIFIED_INT) {
            int result = 0;
            int resourceId = parent.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = parent.getContext().getResources().getDimensionPixelSize(resourceId);
            }
            mTargetY += result;
        }

        isPrepared = true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == mDependViewId;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (!isPrepared) {
            prepare(parent, child, dependency);
        }
        updateView(child, dependency);
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        boolean bool = super.onLayoutChild(parent, child, layoutDirection);
        if (isPrepared) {
            updateView(child, parent.getDependencies(child).get(0));
        }
        return bool;
    }

    public void updateView(View child, View dependency) {
        float percent = 0;
        float start = 0;
        float current = 0;
        float end = UNSPECIFIED_INT;
        switch (mDependType) {
            case DEPEND_TYPE_WIDTH:
                start = mDependStartWidth;
                current = dependency.getWidth();
                end = mDependTargetWidth;
                break;
            case DEPEND_TYPE_HEIGHT:
                start = mDependStartHeight;
                current = dependency.getHeight();
                end = mDependTargetHeight;
                break;
            case DEPEND_TYPE_X:
                start = mDependStartX;
                current = dependency.getX();
                end = mDependTargetX;
                break;
            case DEPEND_TYPE_Y:
                start = mDependStartY;
                current = dependency.getY();
                end = mDependTargetY;
                break;
        }

        if (end != UNSPECIFIED_INT) {
            percent = Math.abs(current - start) / Math.abs(end - start);
        }
        updateViewWithPercent(child, percent > 1 ? 1 : percent);
    }




    public void updateViewWithPercent(View child, float percent) {
        if (mAnimation == null) {
            ViewGroup.LayoutParams childParams = child.getLayoutParams();
            float newX = mTargetX == UNSPECIFIED_INT ? 0 : (mTargetX - mStartX) * percent;
            float newY = mTargetY == UNSPECIFIED_INT ? 0 : (mTargetY - mStartY) * percent;

            if (mTargetWidth != UNSPECIFIED_INT) {
                //int newWidth = (int) (mStartWidth + (mTargetWidth - mStartWidth)*percent);
                float tempX = mStartWidth + ((mTargetWidth - mStartWidth) * percent);
                //childParams.width = newWidth;
                //child.setLayoutParams(childParams);

                child.setScaleX(tempX / mStartWidth);

                newX -= (mStartWidth - tempX) / 2;
            }
            if(mTargetHeight != UNSPECIFIED_INT){
                //int newHeight = (int) (mStartHeight + (mTargetHeight - mStartHeight)*percent);
                float tempY = mStartHeight + ((mTargetHeight - mStartHeight) * percent);
                //childParams.height = newHeight;
                //child.setLayoutParams(childParams);

                child.setScaleY(tempY / mStartHeight);

                newY -= (mStartHeight - tempY) / 2;
            }

            child.setTranslationX(newX);
            child.setTranslationY(newY);

            if (mTargetAlpha != UNSPECIFIED_FLOAT) {
                child.setAlpha(mStartAlpha + (mTargetAlpha - mStartAlpha) * percent);
            }

            if (mTargetBackgroundColor != UNSPECIFIED_INT && mStartBackgroundColor != 0) {
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int color = (int) evaluator.evaluate(percent, mStartBackgroundColor, mTargetBackgroundColor);
                child.setBackgroundColor(color);
            }

            if (mTargetRotateX != UNSPECIFIED_FLOAT) {
                child.setRotationX(mStartRotateX + (mTargetRotateX - mStartRotateX) * percent);
            }
            if (mTargetRotateY != UNSPECIFIED_FLOAT) {
                child.setRotationX(mStartRotateY + (mTargetRotateY - mStartRotateY) * percent);
            }
        } else {
            mAnimation.setStartTime(0);
            mAnimation.restrictDuration(100);
            Transformation transformation = new Transformation();
            mAnimation.getTransformation((long) (percent * 100), transformation);
            BehaviorAnimation animation = new BehaviorAnimation(transformation);
            child.startAnimation(animation);
        }

        child.requestLayout();
    }

    protected int dp2px(float dp) {
        return (int) (dp * scale + 0.5f);
    }



    private static class BehaviorAnimation extends Animation {

        private Transformation mTransformation;

        public BehaviorAnimation(Transformation transformation) {
            mTransformation = transformation;
            setDuration(0);
            setFillAfter(true);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            t.compose(mTransformation);
            super.applyTransformation(interpolatedTime, t);
        }
    }
}