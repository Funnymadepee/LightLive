package com.lzm.lightLive.http.request.hy

import android.util.Log
import com.lzm.lightLive.http.CustomGsonConvertFactory.ResponseConverter
import okhttp3.ResponseBody
import java.io.IOException

class HyConverter : ResponseConverter() {
    @Throws(IOException::class)
    public override fun convertResponse(value: ResponseBody): String {
        val valueStr = value.string()
        Log.e("jsonConverter: ", valueStr)
        return valueStr
    }
}