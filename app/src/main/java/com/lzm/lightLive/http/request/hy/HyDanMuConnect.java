package com.lzm.lightLive.http.request.hy;

import android.util.Log;
import com.lzm.lightLive.common.Const;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.danmu.basic.DanMu;
import com.lzm.lightLive.http.danmu.basic.DanMuMessageType;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.danmu.hy.HuYaDanMuData;
import com.lzm.lightLive.http.danmu.hy.HuYaDanMuUserDataTarsBase;
import com.lzm.lightLive.http.request.Draft_re;
import com.lzm.lightLive.http.request.dy.DyRequest;
import com.lzm.lightLive.util.HexUtil;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class HyDanMuConnect {

    private static String heartbeat
            = "00031d0000690000006910032c3c4c56086f6e6c696" +
            "e657569660f4f6e557365724865617274426561747d00" +
            "003c0800010604745265711d00002f0a0a0c160026003" +
            "6076164725f77617046000b1203aef00f2203aef00f3c" +
            "426d5202605c60017c82000bb01f9cac0b8c980ca80c";


    private final Room room;
    private final MessageReceiveListener receiveListener;

    public HyDanMuConnect() {
        room = null;
        receiveListener = null;
        hyDanMuUserDataTarsBase = new HuYaDanMuUserDataTarsBase();
        hyDanMuFormatDataTarsBase = new HuYaDanMuFormatDataTarsBase();
    }

    byte[] heartBeatArray;
    public HyDanMuConnect(Room room, MessageReceiveListener receiver) {
        this.room = room;
        this.receiveListener = receiver;
        try {
            heartBeatArray = HexUtil.decodeHex(heartbeat);
        }catch (Exception e) {
            //ignore.
        }
    }

    public static class MessageReceiveListener {
        public void onReceiver(String danmu) {}
        public void onConnectError(Exception e) {};
        public void onConnectClose(int code, String reason, boolean remote) {};
    }


    byte[] websocketCmdByteArray;

    /** 解析信息的匹配规则 **/
    private static final Pattern YYID =Pattern.compile("lYyid\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern TID =Pattern.compile("lChannelId\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern SID  =Pattern.compile("lSubChannelId\":([0-9]+)", Pattern.MULTILINE);
    private static final Pattern NICK = Pattern.compile("sNick\":\"(\\S+?)\",", Pattern.MULTILINE);

    public void getHtml() {
        Log.e("TAG", "getHtml: " );
        HyHttpRequest hyHttpRequest = RetrofitManager.getHyMRetrofit().create(HyHttpRequest.class);
        hyHttpRequest.queryRoomInfoHtml("521000")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        String s = response.headers().get("Content-Encoding");
                        System.err.println("s: " + s );

                        Headers headers = response.headers();
                        List<String> values = headers.values("Content-Encoding");
                        for (String value : values) {
                            System.err.println("value: " + value);
                        }

                        String body = "";
                        try {
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
                        }catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        
                        System.err.println("body: " + body);

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
                            websocketCmdByteArray = websocketCmd.toByteArray();

                            Log.e("TAG", "onNext: " + new String(websocketCmdByteArray) );

                            WebSocketClient connect = createConnect();
                            connect.connect();
                        } catch (IllegalStateException illegalStateException) {
                            System.err.println(illegalStateException.getMessage());
                            //由监听器进行定时重试
//            logger.warn("{}直播间信息解析错误，稍后将进行重试，传入的直播url：{},用于获取信息的url：{}", liveRoomUrl, interiorLiveRoomUrl);
//            DanMuClientEventResult danMuClientEventResult = new DanMuClientEventResult();
//            danMuClientEventResult.setLiveRoomData(liveRoomData);
//            danMuClientEventResult.setMessage("直播间信息解析错误");
//            eventManager.notify(DanMuClientEventType.ERROR,danMuClientEventResult);
//            logger.debug("堆栈信息",illegalStateException);
                        } catch (URISyntaxException | IOException e) {
                            e.printStackTrace();
                        }
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

    public WebSocketClient createConnect() throws URISyntaxException, IOException {

        return new WebSocketClient(new URI(Const.WEB_SOCKET_HY), new Draft_re()) {
            final DyRequest request = new DyRequest();
            final Thread heartBeatThread = new Thread(() -> {
                while (true) {
                    try {
                        send(HexUtil.decodeHex(heartbeat));
                        Thread.sleep(3000);        //心跳 斗鱼-45秒 虎牙-30秒
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
            });
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                send(websocketCmdByteArray);
//                    send(request.login(room.getRoomId()));      //发送登录请求
//                    send(request.joinGroup(room.getRoomId()));  //发送加入群组请求
                try {
                    send(HexUtil.decodeHex(heartbeat));                  //发送心跳
                }catch (Exception e) {
                    //
                }
                heartBeatThread.start();
            }

            @Override
            public void onMessage(String message) {}

            @Override
            public void onMessage(ByteBuffer byteBuffer) {
//                Charset charset = StandardCharsets.UTF_8;
//                CharBuffer charBuffer = charset.decode(byteBuffer);
//                String byteData = charBuffer.toString();
//                Log.e("onMessage: ", byteData );
                List<DanMu> danMuData = parseMessage(byteBuffer);
                for (DanMu danMuDatum : danMuData) {
                    Log.e("onMessage: ", danMuDatum.toString());
                }
                /*DouYuDanMu parseDanMu = DanMuParserDY.parse(byteData);
                if(null != receiveListener
                        && null != parseDanMu) {
                    receiveListener.onReceiver(parseDanMu);
                }*/
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if(null != receiveListener) {
                    receiveListener.onConnectClose(code, reason, remote);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                if(null != receiveListener) {
                    receiveListener.onConnectError(e);
                }
            }
        };
    }

    private static final long normalDanMuMessageType = 1400L;
    private HuYaDanMuUserDataTarsBase hyDanMuUserDataTarsBase;
    private HuYaDanMuFormatDataTarsBase hyDanMuFormatDataTarsBase;

    public List<DanMu> parseMessage(ByteBuffer byteBufferMessage) {
        //当前为单次单信息模式
        List<DanMu> danMuDataList = new ArrayList<>(1);
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
            danMuData.setMsgType(DanMuMessageType.DAN_MU.getText());
            //导出操作
//            try {
////                danMuExportService.export(danMuData);
//            } catch (IOException ioException) {
//                Log.e("弹幕数据导出出现IO错误：", ioException.getMessage());
//            }
        } else {
            danMuData.setMsgType(DanMuMessageType.OTHER.getText());
        }
        return danMuDataList;
    }

}
