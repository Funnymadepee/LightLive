package com.lzm.lightLive.http.danmu.basic

import org.java_websocket.handshake.ServerHandshake
import java.nio.ByteBuffer

abstract class WebSocketListener {
    fun onError(e: Exception?) {}
    fun onMessage(message: String?) {}
    fun onMessage(byteBuffer: ByteBuffer?) {}
    open fun onMessage(danMu: DanMu) {}
    fun onOpen(handshake: ServerHandshake?) {}
    fun onReady() {}
    fun onClose(code: Int, reason: String?, remote: Boolean) {}
}