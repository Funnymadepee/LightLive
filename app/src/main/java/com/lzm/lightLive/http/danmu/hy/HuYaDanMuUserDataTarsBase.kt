package com.lzm.lightLive.http.danmu.hy

import com.qq.tars.protocol.tars.TarsInputStream
import com.qq.tars.protocol.tars.TarsOutputStream
import com.qq.tars.protocol.tars.TarsStructBase

class HuYaDanMuUserDataTarsBase : TarsStructBase() {
    var huYaUserInfo: HuYaUserInfo? = null
        private set

    override fun writeTo(os: TarsOutputStream) {}
    override fun readFrom(inputStream: TarsInputStream) {
        //解析参数来源：https://github.com/759434091/danmu-crawler/blob/master/huya/pojo/Huya.js
        inputStream.setServerEncoding("utf-8")
        huYaUserInfo = HuYaUserInfo()
        //读此参数时无数据报错，故取消
//        huYaUserInfo.setlUid(is.read(0, 0, false));
        huYaUserInfo!!.lImid = inputStream.read(0, 1, false)
        huYaUserInfo!!.nickName = inputStream.read("", 2, false)
        huYaUserInfo!!.iGender = inputStream.read(0, 3, false)
        huYaUserInfo!!.sAvatarUrl = inputStream.read("", 4, false)
        huYaUserInfo!!.iNobleLevel =inputStream.read(0, 5, false)
    }
}