package com.lzm.lightLive.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.lzm.lightLive.R;
import com.lzm.lightLive.http.bean.Room;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Locale;

public class CommonUtil {

    public static String convertToHeatString(int hotNum) {
        if (hotNum < 1000) return "" + hotNum;
        int exp = (int) (Math.log(hotNum) / Math.log(1000));
        return String.format(Locale.getDefault(), "%.1f %c",
                hotNum / Math.pow(1000, exp),
                "KMGTPE".charAt(exp - 1));
    }

    public static String convertLong2Time(Long time) {
        String timeStr = "";
        if (time == null) return "";
        //时
        long hour = time / 60 / 60;
        //分
        long minutes = time / 60 % 60;
        //秒
        long remainingSeconds = time % 60;
        //判断时分秒是否小于10
        if (hour < 10) {
            timeStr = minutes + "分" + remainingSeconds + "秒";
        } else if (minutes < 10) {
            timeStr = minutes + "分" + remainingSeconds + "秒";
        } else if (remainingSeconds < 10) {
            timeStr = minutes + "分" + "0" + remainingSeconds + "秒";
        } else {
            timeStr = minutes + "分" + remainingSeconds + "秒";
        }
        return timeStr;
    }

    public static String convertInt2K(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        String numStr = new DecimalFormat("#.00").format(num / 10000d);
        String[] ss = numStr.split("\\.");
        if ("00".equals(ss[1])) {
            return ss[0] + "K";
        } else if ('0' == (ss[1].charAt(1))){
            return ss[0] + "." + ss[1].charAt(0) + "K";
        } else{
            return numStr + "K";
        }
    }

    /**
     * 判断是否是wifi
     *
     * @param mContext context
     * @return boolean
     */
    public static boolean currentIsWifiConnected(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * MD5加密
     * @explain java实现
     * @param str
     *            待加密字符串
     * @return 16进制加密字符串
     */
    public static String encrypt2ToMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int generateLiveStatus(String status) {
        int liveStatus;
        switch (status) {
            case "ON":
                liveStatus = Room.LIVE_STATUS_ON;
                break;
            case "REPLAY":
                liveStatus = Room.LIVE_STATUS_REPLAY;
                break;
            default:
                liveStatus = Room.LIVE_STATUS_OFF;
                break;
        }
        return liveStatus;
    }

    public static int generatePlatformDrawable(int platform) {
        int drawableRes;
        switch (platform) {
            case Room.LIVE_PLAT_DY:
                drawableRes = R.drawable.ic_plat_dy;
                break;
            case Room.LIVE_PLAT_HY:
                drawableRes = R.drawable.ic_plat_hy;
                break;
            case Room.LIVE_PLAT_BL:
                drawableRes = R.drawable.ic_plat_bilibili;
                break;
            default:
                drawableRes = R.drawable.ic_live;
                break;
        }
        return drawableRes;
    }
}
