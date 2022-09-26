package com.lzm.lightLive

import android.app.Application
import android.content.Context
import com.kongzue.dialogx.DialogX
import com.lzm.lightLive.util.SpUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = applicationContext
        SpUtils.initSpUtil(applicationContext)
        DialogX.init(this)
    }

    companion object {
        lateinit var applicationContext: Context
    }
}