package com.lzm.lightLive

import android.app.Application
import android.content.Context
import com.kongzue.dialogx.DialogX
import com.lzm.lib_base.util.AppActivityStateWatcher
import com.lzm.lightLive.util.SpUtils

class App : Application() {

    companion object {
        lateinit var applicationContext: Context
        lateinit var appStateWatcher: AppActivityStateWatcher
    }

    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = applicationContext
        appStateWatcher = AppActivityStateWatcher(this@App)
        appStateWatcher.start()
        SpUtils.initSpUtil(applicationContext)
        DialogX.init(this)
    }

}