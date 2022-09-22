package com.lzm.lightLive.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.TextView;

import com.lzm.lightLive.R;

public class UiTools {

    private static final String TAG = "UiTools";

    public static int px2dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static float pxToDp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (px / scale + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static float dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dp * scale + 0.5f);
    }

    /**
     * 获取textview一行最大能显示几个字(需要在TextView测量完成之后)
     *
     * @param text     文本内容
     * @param paint    textview.getPaint()
     * @param maxWidth textview.getMaxWidth()/或者是指定的数值,如200dp
     */
    public static int getLineMaxNumber(String text, TextPaint paint, int maxWidth) {
        if (null == text || "".equals(text)) {
            return 0;
        }
        StaticLayout staticLayout = new StaticLayout(text, paint, maxWidth, Layout.Alignment.ALIGN_NORMAL
                , 1.0f, 0, false);
        //获取第一行最后显示的字符下标
        return staticLayout.getLineEnd(0);
    }

    /**
     * 设置View的饱和度 Set the saturation of the view
     * @param view the view to be set with saturation
     * @param saturation the saturation value
     * */
    public static void setViewSaturation(View view, float saturation) {
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(saturation);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        view.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    /**
     * 设置view变为灰色 Set specific view to gray or not
     * @param view the view to be set
     * */
    public static void setViewGray(View view) {
        if (view == null) return;
        boolean viewIsGray = false;
        Object tag = view.getTag(R.id.gray_tag);
        if(null == tag) {
            view.setTag(R.id.gray_tag, false);
        }else {
            viewIsGray = (boolean) tag;
        }
        if(viewIsGray){
            view.setLayerType(View.LAYER_TYPE_HARDWARE, new Paint());
        }else {
            setViewSaturation(view, 0);
        }
        view.setTag(R.id.gray_tag, !viewIsGray);
    }

    public static void setNewTextView(SpannableString string, TextView originTextView, TextView newTextView) {
        originTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                TextPaint paint = originTextView.getPaint();
                float wordWidth = paint.measureText(string, 0, 1);
                int measuredWidth = originTextView.getMeasuredWidth();//newTextView.getResources().getDisplayMetrics().widthPixels;
                int maxNum = Math.round(measuredWidth / wordWidth);
                if (maxNum > string.length()) {
                    newTextView.setText(string.subSequence(maxNum, string.length()));
                    newTextView.setVisibility(View.VISIBLE);
                } else {
                    newTextView.setVisibility(View.GONE);
                }
                originTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public static final int getScreenAverageCorner(Activity activity) {
        int[] screenCorner = getScreenCorner(activity);
        int average = 0;
        for (int i : screenCorner) {
            average += i;
        }
        return average / 4;
    }

    public static int[] getScreenCorner(Activity activity) {
        int[] corners = new int[4];
        if(Build.VERSION.SDK_INT > 30) {
            WindowInsets windowInsets = activity.getWindowManager()
                    .getCurrentWindowMetrics()
                    .getWindowInsets();
            try {
                corners[0] = windowInsets.getRoundedCorner(RoundedCorner.POSITION_TOP_LEFT).getRadius();
                corners[1] = windowInsets.getRoundedCorner(RoundedCorner.POSITION_TOP_RIGHT).getRadius();
                corners[2] = windowInsets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT).getRadius();
                corners[3] = windowInsets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_RIGHT).getRadius();
            }catch (NullPointerException e) {
                //ignore this
            }
        }
        return corners;
    }

    public static void setStatusBar(Activity activity, boolean isImmersive) {
        if (isImmersive) {
            activity.getWindow().getDecorView().setSystemUiVisibility(0);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static int convertRGBA(int color) {
        int alpha = color >>> 24;
        int r = ( color & 0xff0000 ) >> 16;
        int g = ( color & 0xff00 ) >> 8;
        int b = color & 0xff;
        return Color.argb(alpha, r, g, b);
    }
}
