package com.lzm.lightLive.http.danmu.basic

import android.content.Context
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.request.Draft_re
import org.java_websocket.client.WebSocketClient
import org.java_websocket.exceptions.WebsocketNotConnectedException
import org.java_websocket.handshake.ServerHandshake
import java.io.IOException
import java.net.URI
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class DanMuServiceConnector(
    protected var mContext: Context?,
    protected val room: Room?,
    protected var listener: WebSocketListener?
) {
    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private var webSocketClient: WebSocketClient? = null
    fun start() {
        executorService.submit {
            webSocketClient = create()
            webSocketClient!!.connect()
        }
    }

    protected open fun socketOnOpen(handshake: ServerHandshake?) {}
    protected open fun socketOnMessage(byteBuffer: ByteBuffer?) {}
    protected fun socketOnClose(code: Int, reason: String?, remote: Boolean) {}
    protected fun socketOnError(ex: Exception?) {}
    protected abstract fun heartBeatInterval(): Int
    protected abstract val webSocketUrl: String?
    @Throws(IOException::class)
    protected abstract fun generateHeartBeatData(): ByteArray?
    fun create(): WebSocketClient {
        return object : WebSocketClient(URI.create(webSocketUrl), Draft_re()) {
            val heartBeatThread = Thread {
                while (true) {
                    try {
                        send(generateHeartBeatData())
                        Thread.sleep(heartBeatInterval().toLong()) //心跳 斗鱼-45秒 虎牙-30秒
                    } catch (e: InterruptedException) {
//                        e.printStackTrace();
                    } catch (e: WebsocketNotConnectedException) {
                    } catch (e: IOException) {
                    }
                }
            }

            override fun onOpen(handshake: ServerHandshake) {
                socketOnOpen(handshake)
                heartBeatThread.start()
            }

            override fun onMessage(message: String) {}
            override fun onMessage(byteBuffer: ByteBuffer) {
                socketOnMessage(byteBuffer)
            }

            override fun onClose(code: Int, reason: String, remote: Boolean) {
                socketOnClose(code, reason, remote)
            }

            override fun onError(ex: Exception) {
                socketOnError(ex)
            }
        }
    }

    fun send(bytes: ByteArray?) {
        if (null != webSocketClient) {
            webSocketClient!!.send(bytes)
        }
    }

    fun close() {
        if (null != webSocketClient) {
            webSocketClient!!.close()
        }
    }

}