package com.lzm.lightLive.util

import android.content.Context
import android.provider.Settings

object PermissionUtil {
    fun canDrawOverlays(context: Context?): Boolean {
        return Settings.canDrawOverlays(context)
    }
}