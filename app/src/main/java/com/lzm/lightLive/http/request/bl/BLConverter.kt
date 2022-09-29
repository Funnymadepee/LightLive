package com.lzm.lightLive.http.request.bl

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.CustomGsonConvertFactory.ResponseConverter
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.bl.BLRoom
import com.lzm.lightLive.http.bean.hy.HyRoom
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.IOException

class BLConverter : ResponseConverter() {
//    companion object {
//        val mBLCall: BlHttpRequest = RetrofitManager.blRetrofit.create(BlHttpRequest::class.java)
//    }

    @Throws(IOException::class)
    public override fun convertResponse(value: ResponseBody): String {
        val valueStr = value.string()
        Log.e("jsonConverter: ", valueStr)
//        val obj = Gson().fromJson(valueStr, JsonObject::class.java)
//        if (obj.has("data") && null != obj["data"]) {
//            var blRoom = Gson().fromJson(obj["data"], BLRoom::class.java)
//            if (blRoom?.uid!! > 0L) {
//
//            }
//        }
        return valueStr
    }
}