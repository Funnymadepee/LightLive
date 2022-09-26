package com.lzm.lightLive.http.request.hy

import com.lzm.lightLive.http.CustomGsonConvertFactory.ResponseConverter
import okhttp3.ResponseBody
import java.io.IOException

class HyRecommendConverter : ResponseConverter() {
    @Throws(IOException::class)
    override fun convertResponse(value: ResponseBody): String {
        val valueStr = value.string()
        return valueStr.substring("getLiveListJsonpCallback(".length, valueStr.length - 1)
    }
}