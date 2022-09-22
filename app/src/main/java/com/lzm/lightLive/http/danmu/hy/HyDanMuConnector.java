package com.lzm.lightLive.http.danmu.hy;

import android.util.Log;

import com.lzm.lightLive.common.Const;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.danmu.basic.DanMu;
import com.lzm.lightLive.http.danmu.basic.DanMuMessageType;
import com.lzm.lightLive.http.danmu.basic.DanMuServiceConnector;
import com.lzm.lightLive.http.danmu.basic.WebSocketListener;
import com.lzm.lightLive.http.request.hy.HuYaDanMuFormatDataTarsBase;
import com.lzm.lightLive.http.request.hy.HyHttpRequest;
import com.lzm.lightLive.util.HexUtil;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;

import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class HyDanMuConnector extends DanMuServiceConnector {

    private static final String TAG = "HyDanMuConnector";

    private static String heartbeat
            = "00031d0000690000006910032c3c4c56086f6e6c696" +
            "e657569660f4f6e557365724865617274426561747d00" +
            "003c0800010604745265711d00002f0a0a0c160026003" +
            "6076164725f77617046000b1203aef00f2203aef00f3c" +
            "426d5202605c60017c82000bb01f9cac0b8c980ca80c";

    private byte[] heartBeatArray;
    private byte[] cmdByteArray;

    /** 解析信息的匹配规则 **/
    private static final Pattern YYID =Pattern.compile("lYyid\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern TID =Pattern.compile("lChannelId\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern SID  =Pattern.compile("lSubChannelId\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern NICK = Pattern.compile("sNick\":\"(\\S+?)\",", Pattern.MULTILINE);

    private HuYaDanMuUserDataTarsBase hyDanMuUserDataTarsBase;
    private HuYaDanMuFormatDataTarsBase hyDanMuFormatDataTarsBase;

    public HyDanMuConnector(Room room, WebSocketListener listener) {
        super(room, listener);
        try {
            heartBeatArray = HexUtil.decodeHex(heartbeat);
        }catch (Exception e) {
            //ignore.
            Log.e(TAG, "HyDanMuConnector: " + e.getMessage() );
        }
        hyDanMuUserDataTarsBase = new HuYaDanMuUserDataTarsBase();
        hyDanMuFormatDataTarsBase = new HuYaDanMuFormatDataTarsBase();
        getHtml(room.getRoomId(), listener);
    }

    @Override
    protected int heartBeatInterval() {
        return 3600;
    }

    @Override
    protected String getWebSocketUrl() {
        return Const.WEB_SOCKET_HY;
    }

    @Override
    protected byte[] generateHeartBeatData() throws IOException {
        return HexUtil.decodeHex(heartbeat);
    }

    public void getHtml(String roomId, WebSocketListener listener) {
        HyHttpRequest hyHttpRequest = RetrofitManager.getHyMRetrofit().create(HyHttpRequest.class);
        hyHttpRequest.queryRoomInfoHtml(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        Log.e(TAG, "onNext: requestHyHtml" );
                        requestHyHtml(response, listener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError: ", e.getMessage() );
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void requestHyHtml(Response<ResponseBody> response, WebSocketListener listener) {
        String s = response.headers().get("Content-Encoding");
        String body = "";
        try {
            if ("gzip".equals(s)) {
                Log.e(TAG, "requestHyHtml: 页面使用gzip压缩");
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
                Log.e(TAG, "requestHyHtml: 页面未压缩");
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                body = response.body().string();
            }
        }catch (Exception e) {
            Log.e(TAG, "requestHyHtml: " + e.getMessage() );
        }

        Matcher liveAnchorMatcher = NICK.matcher(body);
        if (liveAnchorMatcher.find()) {
            String liveAnchorName = liveAnchorMatcher.group(1);
            Log.e(TAG, "requestHyHtml: liveAnchorName: " + liveAnchorName);
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
                Log.e(TAG, "requestHyHtml: "
                        + String.format("ayyuid: %s, tid:%s ,sid:%s%n", ayyuidString, tidString, sidString));
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
            cmdByteArray = websocketCmd.toByteArray();

            Log.e("TAG", "cmdByteArray: " + new String(cmdByteArray) );

            start();
        } catch (IllegalStateException illegalStateException) {
            Log.e(TAG, "requestHyHtml: "+ illegalStateException.getMessage() );
        }
    }

    @Override
    protected void socketOnOpen(ServerHandshake handshake) {
        super.socketOnOpen(handshake);
        send(cmdByteArray);
        try {
            send(HexUtil.decodeHex(heartbeat));                  //发送心跳
        }catch (Exception e) {
            Log.e(TAG, "socketOnOpen: " + e.getMessage() );
        }
    }

    @Override
    protected void socketOnMessage(ByteBuffer byteBuffer) {
        super.socketOnMessage(byteBuffer);
        List<DanMu> danMuList = parseMessage(byteBuffer);
        for (DanMu danMu : danMuList) {
            Log.e(TAG, "socketOnMessage: " + danMuList);
            listener.onMessage(danMu);
        }
    }

    private static final long normalDanMuMessageType = 1400L;

    public List<DanMu> parseMessage(ByteBuffer byteBufferMessage) {
        //当前为单次单信息模式
        List<DanMu> danMuDataList = new ArrayList<>(10);
        DanMu danMuData = new HuYaDanMuData();
        danMuDataList.add(danMuData);

        TarsInputStream tarsInputStream = new TarsInputStream(byteBufferMessage);
        //TODO 解决部分直播弹幕用户无法记录的问题（完全没有收到任何消息,可能是API原因）
        int messageValue1 = tarsInputStream.read(0, 0, false);
//        logger.debug("messageValue1读取值{}",messageValue1);
        if (messageValue1 == 7) {
            //此处传byte[0]，表示让返回值返回为byte[]类型，实际输出与传入数组无关
            byte[] tempArray = new byte[0];
            tarsInputStream = new TarsInputStream(tarsInputStream.read(tempArray, 1, false));

            //real-url中本身是传Int64类型
            long messageTypeValue = tarsInputStream.read(0L, 1, false);
//            logger.debug("tarsInputStream读取值{}",messageTypeValue);

            //1001=贵族续费广播,1400=弹幕消息，8006=(贵族)进房,6501=礼物,6502=全服礼物
            if (messageTypeValue == normalDanMuMessageType) {

                tarsInputStream = new TarsInputStream(tarsInputStream.read(tempArray, 2, false));
                //默认为GBK，需转为utf-8
                tarsInputStream.setServerEncoding("utf-8");

                //用户信息
                TarsStructBase tempBase = tarsInputStream.read(hyDanMuUserDataTarsBase, 0, false);
                if (tempBase != null) {
                    HuYaDanMuUserDataTarsBase dataBase = (HuYaDanMuUserDataTarsBase) tempBase;
                    danMuData.setUserIfo(dataBase.getHuYaUserInfo());
                }

                //内容
                danMuData.setContent(tarsInputStream.read("", 3, false));
                //设置获取时间
                danMuData.setTimestamp(System.currentTimeMillis());

                //弹幕格式
                TarsStructBase tempBase2 = tarsInputStream.read(hyDanMuFormatDataTarsBase, 6, false);
                if (tempBase2 != null) {
                    HuYaDanMuFormatDataTarsBase dataBase = (HuYaDanMuFormatDataTarsBase) tempBase2;
                    danMuData.setDanMuFormatData(dataBase.getDanMuFormatData());
                }
            }
        }
        //设置收到的消息类型，有用户信息时为弹幕，进行导出(后续增加自定义功能)
        if (danMuData.getUserIfo()!=null ) {
            danMuData.setMsgType(DanMuMessageType.DAN_MU);
        } else {
            danMuData.setMsgType(DanMuMessageType.OTHER);
        }
        return danMuDataList;
    }
}
