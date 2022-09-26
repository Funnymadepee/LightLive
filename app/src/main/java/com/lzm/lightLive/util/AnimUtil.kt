package com.lzm.lightLive.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object AnimUtil {
    fun alphaAnim(view: View?, duration: Long, vararg values: Float) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", *values)
        alpha.duration = duration
        alpha.start()
    }

    fun translationYAnim(view: View?, duration: Long, vararg values: Float) {
        val translationY = ObjectAnimator.ofFloat(
            view, "translationY", *values
        )
        translationY.duration = duration
        translationY.start()
    }

    val set = AnimatorSet()
    fun glance(view: View, duration: Long) {
        view.visibility = View.VISIBLE
        val yIn = ObjectAnimator.ofFloat(
            view, "translationY", 0f, view.height.toFloat()
        )
        val yOut = ObjectAnimator.ofFloat(
            view, "translationY", view.height.toFloat(), 0f
        )
        yIn.duration = duration / 2
        yOut.duration = duration / 2

        set.duration = duration
        set.play(yIn).after(yOut)
        if (!set.isRunning) {
            set.start()
        }
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
                view.visibility = View.INVISIBLE
            }
        })
    }
}