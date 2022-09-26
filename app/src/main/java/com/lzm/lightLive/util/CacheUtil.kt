package com.lzm.lightLive.util

import android.content.Context

object CacheUtil {
    private const val NO_PROMPT_CELL_DATA_DIALOG = "NO_PROMPT_CELL_DATA_DIALOG"
    private const val NAV_BAR_IS_NEST_SCROLL = "NAV_BAR_IS_NEST_SCROLL"
    private const val FLOAT_WHEN_EXIT = "FLOAT_WHEN_EXIT"
    fun setNoPromptCellDataDialog(context: Context, noPromptCellDataDialog: Boolean) {
        SpUtils.getInstance(context)!!
            .put(NO_PROMPT_CELL_DATA_DIALOG, noPromptCellDataDialog)
    }

    fun getNoPromptCellDataDialog(context: Context): Boolean {
        return SpUtils.getInstance(context)!![NO_PROMPT_CELL_DATA_DIALOG, false] as Boolean
    }

    fun getNavBarIsNestScroll(context: Context): Boolean {
        return SpUtils.getInstance(context)!!.get(NAV_BAR_IS_NEST_SCROLL, true) as Boolean
    }

    fun setNavBarIsNestScroll(context: Context, navBarIsNestScroll: Boolean) {
        SpUtils.getInstance(context)!!
            .put(NAV_BAR_IS_NEST_SCROLL, navBarIsNestScroll)
    }

    fun getFloatWhenExit(context: Context): Boolean {
        return SpUtils.getInstance(context)!![FLOAT_WHEN_EXIT, true] as Boolean
    }

    fun setFloatWhenExit(context: Context, floatWhenExit: Boolean) {
        SpUtils.getInstance(context)!!.put(FLOAT_WHEN_EXIT, floatWhenExit)
    }
}