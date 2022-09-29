package com.lzm.lightLive.http.danmu.hy

import android.content.Context
import android.util.Log
import com.lzm.lightLive.common.Const
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.danmu.basic.*
import com.lzm.lightLive.http.request.hy.HuYaDanMuFormatDataTarsBase
import com.lzm.lightLive.http.request.hy.HyHttpRequest
import com.lzm.lightLive.util.*
import com.qq.tars.protocol.tars.TarsInputStream
import com.qq.tars.protocol.tars.TarsOutputStream
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.java_websocket.handshake.ServerHandshake
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream
import kotlin.ByteArray
import kotlin.Exception
import kotlin.IllegalStateException
import kotlin.Int
import kotlin.String
import kotlin.Throwable
import kotlin.Throws
import kotlin.also

class HyDanMuConnector(context: Context?, room: Room?, listener: WebSocketListener?) :
    DanMuServiceConnector(context, room, listener) {

    private var heartBeatArray: ByteArray? = null
    private lateinit var cmdByteArray: ByteArray

    private val hyDanMuUserDataTarsBase: HuYaDanMuUserDataTarsBase
    private val hyDanMuFormatDataTarsBase: HuYaDanMuFormatDataTarsBase
    override fun heartBeatInterval(): Int {
        return 3600
    }

    override val webSocketUrl: String
        get() = Const.WEB_SOCKET_HY

    @Throws(IOException::class)
    override fun generateHeartBeatData(): ByteArray {
        return HexUtil.decodeHex(heartbeat)
    }

    private fun getHtml(roomId: String?) {
        val hyHttpRequest = RetrofitManager.hyMobileRetrofit.create(
            HyHttpRequest::class.java
        )
        hyHttpRequest.queryRoomInfoHtml(roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ResponseBody>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(response: Response<ResponseBody>) {
                    Log.e(TAG, "onNext: requestHyHtml")
                    requestHyHtml(response)
                }

                override fun onError(e: Throwable) {
                    Log.e("onError: ", e.message!!)
                }

                override fun onComplete() {}
            })
    }

    fun requestHyHtml(response: Response<ResponseBody>) {
        val s = response.headers()["Content-Encoding"]
        var body = ""
        try {
            if ("gzip" == s) {
                Log.e(TAG, "requestHyHtml: 页面使用gzip压缩")
                val os = ByteArrayOutputStream()
                val `is`: InputStream = GZIPInputStream(response.body()!!.byteStream())
                var len: Int
                val bytes = ByteArray(1024)
                while (`is`.read(bytes).also { len = it } != -1) {
                    os.write(bytes, 0, len)
                }
                `is`.close()
                body = os.toString("utf-8")
                os.close()
            } else {
                Log.e(TAG, "requestHyHtml: 页面未压缩")
                body = response.body()!!.string()
            }
        } catch (e: Exception) {
            Log.e(TAG, "requestHyHtml: " + e.message)
        }
        val liveAnchorMatcher = NICK.matcher(body)
        if (liveAnchorMatcher.find()) {
            val liveAnchorName = liveAnchorMatcher.group(1)
            Log.e(TAG, "requestHyHtml: liveAnchorName: $liveAnchorName")
        }
        try {
            //获取各tar解析参数
            val yyidMatcher = YYID.matcher(body)
            yyidMatcher.find()
            val tidMatcher = TID.matcher(body)
            tidMatcher.find()
            val sidMatcher = SID.matcher(body)
            sidMatcher.find()
            var ayyuidString = ""
            var tidString = ""
            var sidString = ""
            try {
                ayyuidString = yyidMatcher.group(1) as String
                tidString = tidMatcher.group(1) as String
                sidString = sidMatcher.group(1) as String
                Log.e(
                    TAG, "requestHyHtml: "
                            + String.format(
                        "ayyuid: %s, tid:%s ,sid:%s%n",
                        ayyuidString,
                        tidString,
                        sidString
                    )
                )
            } catch (illegalStateException: IllegalStateException) {
                //由监听器进行定时重试
            }
            val ayyuid = ayyuidString.toLong()
            val tid = tidString.toLong()
            val sid = sidString.toLong()

            //Tars解析相关 参考https://github.com/wbt5/real-url/blob/master/danmu/danmaku/huya.py
            val tarsOutputStream = TarsOutputStream()
            //写入解析模式
            tarsOutputStream.write(ayyuid, 0)
            tarsOutputStream.write(true, 1)
            tarsOutputStream.write("", 2)
            tarsOutputStream.write("", 3)
            tarsOutputStream.write(tid, 4)
            tarsOutputStream.write(sid, 5)
            tarsOutputStream.write(0, 6)
            tarsOutputStream.write(0, 7)

            //ws解析指令
            val websocketCmd = TarsOutputStream()
            websocketCmd.write(1, 0)
            websocketCmd.write(tarsOutputStream.toByteArray(), 1)
            cmdByteArray = websocketCmd.toByteArray()
            Log.e("TAG", "cmdByteArray: " + String(cmdByteArray))
            listener?.onReady()
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, "requestHyHtml: " + illegalStateException.message)
        }
    }

    override fun socketOnOpen(handshake: ServerHandshake?) {
        super.socketOnOpen(handshake)
        send(cmdByteArray)
        try {
            send(HexUtil.decodeHex(heartbeat)) //发送心跳
        } catch (e: Exception) {
            Log.e(TAG, "socketOnOpen: " + e.message)
        }
    }

    override fun socketOnMessage(byteBuffer: ByteBuffer?) {
        super.socketOnMessage(byteBuffer)
        val danMuList = parseMessage(byteBuffer)
        for (danMu in danMuList) {
            Log.e(TAG, "socketOnMessage: $danMuList")
            listener!!.onMessage(danMu)
        }
    }

    fun parseMessage(byteBufferMessage: ByteBuffer?): List<DanMu> {
        //当前为单次单信息模式
        val danMuDataList: MutableList<DanMu> = ArrayList(10)
        val danMuData: DanMu = HuYaDanMuData()
        danMuDataList.add(danMuData)
        var tarsInputStream = TarsInputStream(byteBufferMessage)
        //TODO 解决部分直播弹幕用户无法记录的问题（完全没有收到任何消息,可能是API原因）
        val messageValue1 = tarsInputStream.read(0, 0, false)
        //        logger.debug("messageValue1读取值{}",messageValue1);
        if (messageValue1 == 7) {
            //此处传byte[0]，表示让返回值返回为byte[]类型，实际输出与传入数组无关
            val tempArray = ByteArray(0)
            tarsInputStream = TarsInputStream(tarsInputStream.read(tempArray, 1, false))

            //real-url中本身是传Int64类型
            val messageTypeValue = tarsInputStream.read(0L, 1, false)
            //            logger.debug("tarsInputStream读取值{}",messageTypeValue);

            //1001=贵族续费广播,1400=弹幕消息，8006=(贵族)进房,6501=礼物,6502=全服礼物
            if (messageTypeValue == normalDanMuMessageType) {
                tarsInputStream = TarsInputStream(tarsInputStream.read(tempArray, 2, false))
                //默认为GBK，需转为utf-8
                tarsInputStream.setServerEncoding("utf-8")

                //用户信息
                val tempBase = tarsInputStream.read(hyDanMuUserDataTarsBase, 0, false)
                if (tempBase != null) {
                    val dataBase = tempBase as HuYaDanMuUserDataTarsBase
                    danMuData.userIfo = dataBase.huYaUserInfo
                }

                //内容
                danMuData.content = tarsInputStream.read("", 3, false)
                //设置获取时间
                danMuData.timestamp = System.currentTimeMillis()

                //弹幕格式
                val tempBase2 = tarsInputStream.read(hyDanMuFormatDataTarsBase, 6, false)
                if (tempBase2 != null) {
                    val dataBase = tempBase2 as HuYaDanMuFormatDataTarsBase
                    danMuData.danMuFormatData = dataBase.danMuFormatData
                }
            }
        }
        //设置收到的消息类型，有用户信息时为弹幕，进行导出(后续增加自定义功能)
        if (danMuData.userIfo != null) {
            danMuData.msgType = DanMuMessageType.DAN_MU
        } else {
            danMuData.msgType = DanMuMessageType.OTHER
        }
        return danMuDataList
    }

    companion object {
        private const val TAG = "HyDanMuConnector"
        private const val heartbeat = "00031d0000690000006910032c3c4c56086f6e6c696" +
                "e657569660f4f6e557365724865617274426561747d00" +
                "003c0800010604745265711d00002f0a0a0c160026003" +
                "6076164725f77617046000b1203aef00f2203aef00f3c" +
                "426d5202605c60017c82000bb01f9cac0b8c980ca80c"

        /** 解析信息的匹配规则  */
        private val YYID = Pattern.compile("lYyid\":([0-9]+)", Pattern.MULTILINE)
        private val TID = Pattern.compile("lChannelId\":([0-9]+)", Pattern.MULTILINE)
        private val SID = Pattern.compile("lSubChannelId\":([0-9]+)", Pattern.MULTILINE)
        private val NICK = Pattern.compile("sNick\":\"(\\S+?)\",", Pattern.MULTILINE)
        private const val normalDanMuMessageType = 1400L
    }

    init {
        try {
            heartBeatArray = HexUtil.decodeHex(heartbeat)
        } catch (e: Exception) {
            //ignore.
            Log.e(TAG, "HyDanMuConnector: " + e.message)
        }
        hyDanMuUserDataTarsBase = HuYaDanMuUserDataTarsBase()
        hyDanMuFormatDataTarsBase = HuYaDanMuFormatDataTarsBase()
        getHtml(room?.roomId)
    }
}