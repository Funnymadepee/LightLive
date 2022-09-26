package com.lzm.lightLive.http.request.hy

import com.qq.tars.protocol.tars.TarsInputStream
import com.qq.tars.protocol.tars.TarsOutputStream
import com.qq.tars.protocol.tars.TarsStructBase

class HuYaDanMuFormatDataTarsBase : TarsStructBase() {
    var danMuFormatData: HyDanMuFormatData? = null
        private set

    override fun writeTo(os: TarsOutputStream) {}
    override fun readFrom(inputStream: TarsInputStream) {
        inputStream.setServerEncoding("utf-8")
        danMuFormatData = HyDanMuFormatData()
        danMuFormatData?.fontColor = inputStream.read(0, 0, false)
        danMuFormatData?.fontSize = inputStream.read(0, 1, false)
        danMuFormatData?.textSpeed = inputStream.read(0, 2, false)
        danMuFormatData?.transitionType = inputStream.read(0, 3, false)
        danMuFormatData?.popupStyle = inputStream.read(0, 4, false)
    }

    class HyDanMuFormatData : DanMuFormat()
}