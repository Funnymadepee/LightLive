package com.lzm.lightLive.util

import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class SpUtils private constructor(context: Context) {
    fun put(key: String?, obj: Any) {
        val editor = sp!!.edit()
        when (obj) {
            is String -> {
                editor.putString(key, obj)
            }
            is Int -> {
                editor.putInt(key, obj)
            }
            is Boolean -> {
                editor.putBoolean(key, obj)
            }
            is Float -> {
                editor.putFloat(key, obj)
            }
            is Long -> {
                editor.putLong(key, obj)
            }
            else -> {
                editor.putString(key, obj.toString())
            }
        }
        SharedPreferencesCompat.apply(editor)
    }

    fun putString(key: String?, stringData: String?) {
        val editor = sp!!.edit()
        editor.putString(key, stringData)
        editor.apply()
    }

    operator fun get(key: String?, defaultObject: Any?): Any? {
        when (defaultObject) {
            is String -> {
                return sp!!.getString(key, defaultObject as String?)
            }
            is Int -> {
                return sp!!.getInt(key, (defaultObject as Int?)!!)
            }
            is Boolean -> {
                return sp!!.getBoolean(key, (defaultObject as Boolean?)!!)
            }
            is Float -> {
                return sp!!.getFloat(key, (defaultObject as Float?)!!)
            }
            is Long -> {
                return sp!!.getLong(key, (defaultObject as Long?)!!)
            }
            else -> return null
        }
    }

    fun getString(key: String?, stringDataDefault: String?): String? {
        return sp!!.getString(key, stringDataDefault)
    }

    fun remove(key: String?) {
        val editor = sp!!.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    fun clear() {
        val editor = sp!!.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    operator fun contains(key: String?): Boolean {
        return sp!!.contains(key)
    }

    val all: Map<String, *>
        get() = sp!!.all

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }
            editor.commit()
        }
    }

    companion object {
        private const val TAG = "mSp_u8xixAoiStrWdfW"
        private var spUtils: SpUtils? = null
        private var sp: SharedPreferences? = null
        @Synchronized
        fun initSpUtil(context: Context) {
            if (spUtils == null) {
                spUtils = SpUtils(context)
            }
        }

        @Synchronized
        fun getInstance(context: Context): SpUtils? {
            if (null == spUtils) {
                spUtils = SpUtils(context)
            }
            return spUtils
        }
    }

    init {
        sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}