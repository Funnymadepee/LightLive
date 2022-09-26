package com.lzm.lightLive.http.request.hy

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.lzm.lightLive.http.CustomGsonConvertFactory.ResponseConverter
import com.lzm.lightLive.http.bean.hy.HyRoom
import com.lzm.lightLive.util.CommonUtil
import okhttp3.ResponseBody
import java.io.IOException

class HyBasicConverter : ResponseConverter() {

    @Throws(IOException::class)
    override fun convertResponse(value: ResponseBody): String {
        var response = value.string()
        val obj = Gson().fromJson(response, JsonObject::class.java)
        Log.e("convertResponse: ", obj.toString())
        if (obj.has("data") && obj["data"] != null) {
            val roomInfo = Gson().fromJson(obj["data"], HyRoom::class.java)
            val roomName = roomInfo.hyLiveData?.roomName
            val userCount = roomInfo.hyLiveData!!.userCount
            val profileRoom = roomInfo.hyLiveData?.profileRoom
            val screenshot = roomInfo.hyLiveData?.screenshot
            val fullName = roomInfo.hyLiveData?.gameFullName
            if (roomInfo.hyStream != null
                && roomInfo!!.hyStream?.flv != null
                && roomInfo.hyStream?.flv?.multiLine?.get(0) != null) {
                val url = roomInfo.hyStream!!.flv?.multiLine?.get(0)?.url
                roomInfo.liveStreamUriHigh = url
            }
            val nick = roomInfo.hyProfileInfo?.nick
            val avatar = roomInfo.hyProfileInfo?.avatar
            val activityCount = roomInfo.hyProfileInfo?.activityCount
            roomInfo.hostName = nick
            roomInfo.roomName = roomName
            roomInfo.cateName = fullName
            roomInfo.hostAvatar = avatar
            roomInfo.heatNum = userCount
            roomInfo.thumbUrl = screenshot
            roomInfo.fansNum = activityCount!!.toLong()
            roomInfo.streamStatus = CommonUtil.generateLiveStatus(roomInfo.hyRealLiveStatus)
            roomInfo.roomId = profileRoom.toString()
            obj.remove("data")
            obj.add("data", Gson().toJsonTree(roomInfo, object : TypeToken<HyRoom?>() {}.type))
            response = obj.toString()
        }
        return response
    }
}