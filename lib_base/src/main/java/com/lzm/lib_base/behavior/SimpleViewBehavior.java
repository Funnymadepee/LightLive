package com.lzm.lib_base.behavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.lzm.lib_base.R;

public class SimpleViewBehavior extends CoordinatorLayout.Behavior<View> {

    private static final int UNSPECIFIED_INT = Integer.MAX_VALUE;
    private static final float UNSPECIFIED_FLOAT = Float.MAX_VALUE;

    private static final int DEPEND_TYPE_HEIGHT = 0;

    private static final int DEPEND_TYPE_WIDTH = 1;

    private static final int DEPEND_TYPE_X = 2;

    private static final int DEPEND_TYPE_Y = 3;

    private final int mDependType;
    private final int mDependViewId;

    private final int mDependTargetX;
    private final int mDependTargetY;
    private final int mDependTargetWidth;
    private final int mDependTargetHeight;

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

    private final int mTargetX;
    private int mTargetY;
    private final int mTargetWidth;
    private final int mTargetHeight;
    private final int mTargetBackgroundColor;
    private final float mTargetAlpha;
    private final float mTargetRotateX;
    private final float mTargetRotateY;

    private final int mAnimationId;
    private Animation mAnimation;

    private boolean isPrepared;

    public SimpleViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
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

        if (parent.getFitsSystemWindows() && mTargetY != UNSPECIFIED_INT) {
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
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent
            , @NonNull View child, View dependency) {
        return dependency.getId() == mDependViewId;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent,
                                          @NonNull  View child,
                                          @NonNull  View dependency) {
        if (!isPrepared) {
            prepare(parent, child, dependency);
        }
        updateView(child, dependency);
        return false;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent,
                                 @NonNull View child,
                                 int layoutDirection) {
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
            float newX = mTargetX == UNSPECIFIED_INT ? 0 : (mTargetX - mStartX) * percent;
            float newY = mTargetY == UNSPECIFIED_INT ? 0 : (mTargetY - mStartY) * percent;

            if (mTargetWidth != UNSPECIFIED_INT) {
                float tempX = mStartWidth + ((mTargetWidth - mStartWidth) * percent);
                child.setScaleX(tempX / mStartWidth);
                newX -= (mStartWidth - tempX) / 2;
            }
            if(mTargetHeight != UNSPECIFIED_INT){
                float tempY = mStartHeight + ((mTargetHeight - mStartHeight) * percent);
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

    private static class BehaviorAnimation extends Animation {

        private final Transformation mTransformation;

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