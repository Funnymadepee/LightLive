package com.lzm.lightLive.http.request.hy

import android.util.Log
import com.qq.tars.protocol.tars.TarsOutputStream
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream
import kotlin.ByteArray
import kotlin.IllegalStateException
import kotlin.Int
import kotlin.String
import kotlin.Throws
import kotlin.also

class RequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e("intercept: ", "")
        val response: Response = chain.proceed(chain.request())
        val s = response.header("Content-Encoding")
        System.err.println(s)
        val headers = response.headers
        val values = headers.values("Content-Encoding")
        for (value in values) {
            System.err.println(value)
        }
        val body: String
        if ("gzip" == s) {
            System.err.println("请求页面使用gzip压缩")
            val os = ByteArrayOutputStream()
            val inputStream: InputStream = GZIPInputStream(response.body.byteStream())
            var len: Int
            val bytes = ByteArray(1024)
            while (inputStream.read(bytes).also { len = it } != -1) {
                os.write(bytes, 0, len)
            }
            inputStream.close()
            body = os.toString("utf-8")
            os.close()
        } else {
            System.err.println("请求页面未压缩")
            body = response.body.string()
        }
        System.err.println(body)
        val liveAnchorMatcher = NICK.matcher(body)
        if (liveAnchorMatcher.find()) {
            val liveAnchorName = liveAnchorMatcher.group(1)
            System.err.println("liveAnchorName: $liveAnchorName")
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
                System.err.printf(
                    "ayyuid: %s, tid:%s ,sid:%s%n",
                    ayyuidString,
                    tidString,
                    sidString
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
            websocketCmd.toByteArray()
        } catch (illegalStateException: IllegalStateException) {
            //由监听器进行定时重试
//            logger.warn("{}直播间信息解析错误，稍后将进行重试，传入的直播url：{},用于获取信息的url：{}", liveRoomUrl, interiorLiveRoomUrl);
//            DanMuClientEventResult danMuClientEventResult = new DanMuClientEventResult();
//            danMuClientEventResult.setLiveRoomData(liveRoomData);
//            danMuClientEventResult.setMessage("直播间信息解析错误");
//            eventManager.notify(DanMuClientEventType.ERROR,danMuClientEventResult);
//            logger.debug("堆栈信息",illegalStateException);
        }
        return response
    }

    companion object {
        /** 解析信息的匹配规则  */
        private val YYID = Pattern.compile("lYyid\":([0-9]+)", Pattern.MULTILINE)
        private val TID = Pattern.compile("lChannelId\":([0-9]+)", Pattern.MULTILINE)
        private val SID = Pattern.compile("lSubChannelId\":([0-9]+)", Pattern.MULTILINE)
        private val NICK = Pattern.compile("sNick\":\"(\\S+?)\",", Pattern.MULTILINE)

        /**
         * 直播间代号匹配正则
         */
        //private val LIVE_ROOM_CODE_PATTERN = Pattern.compile("\\.\\S+/(\\S+)\\??")
    }
}