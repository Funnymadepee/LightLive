package com.lzm.lightLive

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.kongzue.dialogx.DialogX
import com.lzm.lib_base.util.AppActivityStateWatcher
import com.lzm.lightLive.util.SpUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.util.*

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