package com.lzm.lightLive.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.text.Layout
import android.text.SpannableString
import android.text.StaticLayout
import android.text.TextPaint
import android.view.HapticFeedbackConstants
import android.view.RoundedCorner
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.lzm.lightLive.R
import kotlin.math.roundToInt

object UiTools {

//    private const val TAG = "UiTools"

    val states = arrayOf(
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf(android.R.attr.state_focused),
        intArrayOf(android.R.attr.state_activated),
        intArrayOf(android.R.attr.state_selected)
    )
    val colors = intArrayOf(
        R.attr.basic_ripple_state_pressed,
        R.attr.basic_ripple_state_focused,
        R.attr.basic_ripple_state_activated,
        R.attr.basic_ripple_state_selected
    )
    val rippleColorStateList: ColorStateList
        get() = ColorStateList(states, colors)

    fun px2dp(context: Context, px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    fun pxToDp(context: Context, px: Float): Float {
        val scale = context.resources.displayMetrics.density
        return px / scale + 0.5f
    }

    fun dp2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun dpToPx(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    /**
     * 获取textview一行最大能显示几个字(需要在TextView测量完成之后)
     *
     * @param text     文本内容
     * @param paint    textview.getPaint()
     * @param maxWidth textview.getMaxWidth()/或者是指定的数值,如200dp
     */
    fun getLineMaxNumber(text: String?, paint: TextPaint?, maxWidth: Int): Int {
        if (null == text || "" == text) {
            return 0
        }
        val staticLayout = StaticLayout(
            text, paint, maxWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0F, false
        )
        //获取第一行最后显示的字符下标
        return staticLayout.getLineEnd(0)
    }

    /**
     * 设置View的饱和度 Set the saturation of the view
     *
     * @param view       the view to be set with saturation
     * @param saturation the saturation value
     */
    fun setViewSaturation(view: View, saturation: Float) {
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturation)
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        view.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

    /**
     * 设置view变为灰色 Set specific view to gray or not
     *
     * @param view the view to be set
     */
    fun setViewGray(view: View?) {
        if (view == null) return
        var viewIsGray = false
        val tag = view.getTag(R.id.gray_tag)
        if (null == tag) {
            view.setTag(R.id.gray_tag, false)
        } else {
            viewIsGray = tag as Boolean
        }
        if (viewIsGray) {
            view.setLayerType(View.LAYER_TYPE_HARDWARE, Paint())
        } else {
            setViewSaturation(view, 0f)
        }
        view.setTag(R.id.gray_tag, !viewIsGray)
    }

    fun setNewTextView(string: SpannableString, originTextView: TextView, newTextView: TextView) {
        originTextView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val paint = originTextView.paint
                val wordWidth = paint.measureText(string, 0, 1)
                val measuredWidth =
                    originTextView.measuredWidth //newTextView.getResources().getDisplayMetrics().widthPixels;
                val maxNum = (measuredWidth / wordWidth).roundToInt()
                if (maxNum > string.length) {
                    newTextView.text = string.subSequence(maxNum, string.length)
                    newTextView.visibility = View.VISIBLE
                } else {
                    newTextView.visibility = View.GONE
                }
                originTextView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    fun getScreenAverageCorner(activity: Activity): Int {
        val screenCorner = getScreenCorner(activity)
        var average = 0
        for (i in screenCorner) {
            average += i
        }
        return average / 4
    }

    fun getScreenCorner(activity: Activity): IntArray {
        val corners = IntArray(4)
        if (Build.VERSION.SDK_INT > 30) {
            val windowInsets = activity.windowManager
                .currentWindowMetrics
                .windowInsets
            try {
                corners[0] = windowInsets.getRoundedCorner(RoundedCorner.POSITION_TOP_LEFT)!!.radius
                corners[1] =
                    windowInsets.getRoundedCorner(RoundedCorner.POSITION_TOP_RIGHT)!!.radius
                corners[2] = windowInsets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)!!
                    .radius
                corners[3] = windowInsets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_RIGHT)!!
                    .radius
            } catch (e: NullPointerException) {
                //ignore this
            }
        }
        return corners
    }

    /**
     * @param isImmersive 是否显示浅色背景状态栏
     */
    fun setStatusBar(activity: Activity, isImmersive: Boolean) {
        if (isImmersive) {
            activity.window.decorView.systemUiVisibility = 0
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    fun convertRGBA(color: Int): Int {
        val alpha = color ushr 24
        val r = color and 0xff0000 shr 16
        val g = color and 0xff00 shr 8
        val b = color and 0xff
        return Color.argb(alpha, r, g, b)
    }

    fun performHapticClick(view: View) {
        view.performHapticFeedback(
            HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }

    fun performHapticLongPress(view: View) {
        view.performHapticFeedback(
            HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }

    fun circleReveal(view: View, isShow: Boolean) {
        val width = view.width
        val cx = width.div(2)
        val cy = view.height.div(2)
        val anim: Animator = if (isShow) ViewAnimationUtils.createCircularReveal(
            view,
            cx,
            cy,
            0f,
            width.toFloat()
        ) else ViewAnimationUtils.createCircularReveal(view, cx, cy, width.toFloat(), 0f)
        anim.duration = 500
        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.GONE
                }
            }
        })
        // make the view visible and start the animation
        if (isShow) view.visibility = View.VISIBLE else view.visibility = View.GONE
        // start the animation
        anim.start()
    }

    fun snackShort(view: View, @StringRes stringId: Int) {
        Snackbar.make(view, view.resources.getText(stringId), Snackbar.LENGTH_SHORT).show()
    }

    fun snackShort(view: View?, msg: String?) {
        Snackbar.make(view!!, msg!!, Snackbar.LENGTH_SHORT).show()
    }

    fun snackLong(view: View, @StringRes stringId: Int) {
        Snackbar.make(view, view.resources.getText(stringId), Snackbar.LENGTH_LONG).show()
    }

    fun snackLong(view: View?, msg: String?) {
        Snackbar.make(view!!, msg!!, Snackbar.LENGTH_LONG).show()
    }

    fun snackIndefinite(view: View, @StringRes stringId: Int) {
        Snackbar.make(view, view.resources.getText(stringId), Snackbar.LENGTH_INDEFINITE).show()
    }

    fun snackIndefinite(view: View?, msg: String?) {
        Snackbar.make(view!!, msg!!, Snackbar.LENGTH_INDEFINITE).show()
    }
}