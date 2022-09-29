package com.lzm.lightLive.http.danmu.dy

import android.content.Context
import com.lzm.lightLive.common.Const
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.danmu.basic.DanMuServiceConnector
import com.lzm.lightLive.http.danmu.basic.WebSocketListener
import com.lzm.lightLive.http.request.dy.DyRequest
import org.java_websocket.handshake.ServerHandshake
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class DyDanMuConnector(context: Context?, room: Room?, listener: WebSocketListener?) :
    DanMuServiceConnector(context, room, listener) {

    private val request: DyRequest = DyRequest()
    override val webSocketUrl: String
        get() = Const.WEB_SOCKET_DY

    @Throws(IOException::class)
    public override fun generateHeartBeatData(): ByteArray {
        return request.heartBeat()
    }

    public override fun heartBeatInterval(): Int {
        return 4500
    }

    override fun socketOnOpen(handshake: ServerHandshake?) {
        super.socketOnOpen(handshake)
        try {
            send(request.login(room?.roomId)) //发送登录请求
            send(request.joinGroup(room?.roomId)) //发送加入群组请求
            send(request.heartBeat()) //发送心跳
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun socketOnMessage(byteBuffer: ByteBuffer?) {
        super.socketOnMessage(byteBuffer)
        val charset = StandardCharsets.UTF_8
        val charBuffer = charset.decode(byteBuffer)
        val byteData = charBuffer.toString()
        val parseDanMu = DanMuParserDY.parse(mContext!!, byteData)
        if (null != listener
            && null != parseDanMu
        ) {
            listener!!.onMessage(parseDanMu)
        }
    }
}