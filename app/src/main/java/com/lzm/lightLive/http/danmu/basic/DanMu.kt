package com.lzm.lightLive.http.danmu.basic

import com.lzm.lightLive.http.request.hy.DanMuFormat

open class DanMu {
    /**
     * 发送者名称
     */
    var userIfo: DanMuUserInfo? = null

    /**弹幕信息 */
    var content: String? = null

    /**弹幕格式 */
    var danMuFormatData: DanMuFormat? = null

    /**发送时间(时间戳类型) */
    var timestamp: Long? = null

    /**消息类型(弹幕/礼物/其他) */
    var msgType: DanMuMessageType? = null

    constructor()

    constructor(
        userIfo: DanMuUserInfo?,
        content: String?,
        danMuFormatData: DanMuFormat?,
        timestamp: Long?,
        msgType: DanMuMessageType?
    ) {
        this.userIfo = userIfo
        this.content = content
        this.danMuFormatData = danMuFormatData
        this.timestamp = timestamp
        this.msgType = msgType
    }

    override fun toString(): String {
        val sb = StringBuffer("DanMuData{")
        if (userIfo != null) {
            sb.append("userIfo-nickName=").append(userIfo?.nickName)
        }
        sb.append(", formatData='").append(danMuFormatData.toString()).append('\'')
        sb.append(", content='").append(content).append('\'')
        sb.append(", timestamp=").append(timestamp)
        sb.append(", msgType='").append(msgType).append('\'')
        sb.append('}')
        return sb.toString()
    }
}