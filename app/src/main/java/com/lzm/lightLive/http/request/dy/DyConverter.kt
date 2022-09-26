package com.lzm.lightLive.http.request.dy

import android.util.Log
import com.lzm.lightLive.http.CustomGsonConvertFactory.ResponseConverter
import okhttp3.ResponseBody
import java.io.IOException

class DyConverter : ResponseConverter() {
    @Throws(IOException::class)
    override fun convertResponse(value: ResponseBody): String {
        val response = value.string()
        Log.e("TAG", response)
        return response
    }
}