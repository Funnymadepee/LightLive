package com.lzm.lightLive.util

import android.content.Context
import android.net.ConnectivityManager
import com.lzm.lightLive.R
import com.lzm.lightLive.http.bean.Room
import java.math.BigInteger
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

object CommonUtil {
    fun convertToHeatString(hotNum: Int): String {
        if (hotNum < 1000) return "" + hotNum
        val exp = (ln(hotNum.toDouble()) / ln(1000.0)).toInt()
        return String.format(
            Locale.getDefault(), "%.1f %c",
            hotNum / 1000.0.pow(exp.toDouble()),
            "KMGTPE"[exp - 1]
        )
    }

    fun convertLong2Time(time: Long?): String {
        var timeStr: String
        if (time == null) return ""
        //时
        val hour = time / 60 / 60
        //分
        val minutes = time / 60 % 60
        //秒
        val remainingSeconds = time % 60
        //判断时分秒是否小于10
        timeStr = if (hour < 10) {
            minutes.toString() + "分" + remainingSeconds + "秒"
        } else if (minutes < 10) {
            minutes.toString() + "分" + remainingSeconds + "秒"
        } else if (remainingSeconds < 10) {
            minutes.toString() + "分" + "0" + remainingSeconds + "秒"
        } else {
            minutes.toString() + "分" + remainingSeconds + "秒"
        }
        return timeStr
    }

    fun convertInt2K(num: Long): String {
        if (num < 10000) {
            return num.toString()
        }
        val numStr = DecimalFormat("#.00").format(num / 10000.0)
        val ss = numStr.split(".").toTypedArray()
        return if ("00" == ss[1]) {
            ss[0] + "K"
        } else if ('0' == ss[1][1]) {
            ss[0] + "." + ss[1][0] + "K"
        } else {
            numStr + "K"
        }
    }

    /**
     * 判断是否是wifi
     *
     * @param mContext context
     * @return boolean
     */
    fun currentIsWifiConnected(mContext: Context): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * MD5加密
     * @explain java实现
     * @param str
     * 待加密字符串
     * @return 16进制加密字符串
     */
    fun encrypt2ToMD5(str: String): String? {
        return try {
            // 生成一个MD5加密计算摘要
            val md = MessageDigest.getInstance("MD5")
            // 计算md5函数
            md.update(str.toByteArray())
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            BigInteger(1, md.digest()).toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun generateLiveStatus(status: String?): Int {
        val liveStatus: Int = when (status) {
            "ON" -> Room.LIVE_STATUS_ON
            "REPLAY" -> Room.LIVE_STATUS_REPLAY
            else -> Room.LIVE_STATUS_OFF
        }
        return liveStatus
    }

    fun generatePlatformDrawable(platform: Int): Int {
        val drawableRes: Int = when (platform) {
            Room.LIVE_PLAT_DY -> R.drawable.ic_plat_dy
            Room.LIVE_PLAT_HY -> R.drawable.ic_plat_hy
            Room.LIVE_PLAT_BL -> R.drawable.ic_plat_bilibili
            else -> R.drawable.ic_live
        }
        return drawableRes
    }
}