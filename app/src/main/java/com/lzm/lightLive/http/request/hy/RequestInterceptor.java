package com.lzm.lightLive.http.request.hy;

import android.util.Log;

import com.qq.tars.protocol.tars.TarsOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    /** 解析信息的匹配规则 **/
    private static final Pattern YYID =Pattern.compile("lYyid\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern TID =Pattern.compile("lChannelId\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern SID  =Pattern.compile("lSubChannelId\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern NICK = Pattern.compile("sNick\":\"(\\S+?)\",", Pattern.MULTILINE);
    /**
     * 直播间代号匹配正则
     */
    private static final Pattern LIVE_ROOM_CODE_PATTERN = Pattern.compile("\\.\\S+/(\\S+)\\??");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("intercept: ", "" );
        Response response = chain.proceed(chain.request());
        String s = response.header("Content-Encoding");
        System.err.println(s);

        Headers headers = response.headers();
        List<String> values = headers.values("Content-Encoding");
        for (String value : values) {
            System.err.println(value);
        }

        String body;
        if ("gzip".equals(s)) {
            System.err.println("请求页面使用gzip压缩");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream is = new GZIPInputStream(response.body().byteStream());
            int len;
            byte[] bytes = new byte[1024];
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            is.close();
            body = os.toString("utf-8");
            os.close();
        }else {
            System.err.println("请求页面未压缩");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            body = response.body().string();
        }
        System.err.println(body);

        Matcher liveAnchorMatcher = NICK.matcher(body);
        if (liveAnchorMatcher.find()) {
            String liveAnchorName = liveAnchorMatcher.group(1);
            System.err.println("liveAnchorName: " + liveAnchorName);
        }

        try {
            //获取各tar解析参数
            Matcher yyidMatcher = YYID.matcher(body);
            yyidMatcher.find();
            Matcher tidMatcher = TID.matcher(body);
            tidMatcher.find();
            Matcher sidMatcher = SID.matcher(body);
            sidMatcher.find();

            String ayyuidString = "";
            String tidString = "";
            String sidString = "";

            try {
                ayyuidString = yyidMatcher.group(1);
                tidString = tidMatcher.group(1);
                sidString = sidMatcher.group(1);
                System.err.printf("ayyuid: %s, tid:%s ,sid:%s%n", ayyuidString, tidString, sidString);
            } catch (IllegalStateException illegalStateException) {
                //由监听器进行定时重试
            }

            long ayyuid = Long.parseLong(ayyuidString);
            long tid = Long.parseLong(tidString);
            long sid = Long.parseLong(sidString);


            //Tars解析相关 参考https://github.com/wbt5/real-url/blob/master/danmu/danmaku/huya.py
            TarsOutputStream tarsOutputStream = new TarsOutputStream();
            //写入解析模式
            tarsOutputStream.write(ayyuid, 0);
            tarsOutputStream.write(Boolean.TRUE, 1);
            tarsOutputStream.write("", 2);
            tarsOutputStream.write("", 3);
            tarsOutputStream.write(tid, 4);
            tarsOutputStream.write(sid, 5);
            tarsOutputStream.write(0, 6);
            tarsOutputStream.write(0, 7);

            //ws解析指令
            TarsOutputStream websocketCmd = new TarsOutputStream();
            websocketCmd.write(1, 0);
            websocketCmd.write(tarsOutputStream.toByteArray(), 1);
            byte[] websocketCmdByteArray = websocketCmd.toByteArray();
        } catch (IllegalStateException illegalStateException) {
            //由监听器进行定时重试
//            logger.warn("{}直播间信息解析错误，稍后将进行重试，传入的直播url：{},用于获取信息的url：{}", liveRoomUrl, interiorLiveRoomUrl);
//            DanMuClientEventResult danMuClientEventResult = new DanMuClientEventResult();
//            danMuClientEventResult.setLiveRoomData(liveRoomData);
//            danMuClientEventResult.setMessage("直播间信息解析错误");
//            eventManager.notify(DanMuClientEventType.ERROR,danMuClientEventResult);
//            logger.debug("堆栈信息",illegalStateException);
        }
        return response;
    }

}
