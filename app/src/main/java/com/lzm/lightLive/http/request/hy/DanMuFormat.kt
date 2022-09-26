package com.lzm.lightLive.http.request.hy

open class DanMuFormat {
    /**
     * 字体颜色
     */
    var fontColor = 1

    /**
     * 字体大小
     */
    var fontSize = 4

    /**
     * 文本速度
     */
    var textSpeed = 0

    /**
     * 过渡类型
     */
    var transitionType = 1

    /**
     * 是否弹出弹幕
     */
    var popupStyle = 0

    override fun toString(): String {
        val sb = StringBuffer("DanMuFormatData{")
        sb.append("fontColor=").append(fontColor)
        sb.append(", fontSize=").append(fontSize)
        sb.append(", textSpeed=").append(textSpeed)
        sb.append(", transitionType=").append(transitionType)
        sb.append(", popupStyle=").append(popupStyle)
        sb.append('}')
        return sb.toString()
    }
}