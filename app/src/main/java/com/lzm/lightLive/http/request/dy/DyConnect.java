package com.lzm.lightLive.http.request.dy;

import com.lzm.lightLive.common.Const;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.util.DanMuParserDY;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DyConnect {

    private final Room room;
    private final MessageReceiveListener receiveListener;

    public DyConnect(Room room, MessageReceiveListener receiver) {
        this.room = room;
        this.receiveListener = receiver;
    }

    public interface MessageReceiveListener {
        void onReceiver(DouYuDanMu douYuDanMu);
        void onConnectError(Exception e);
        void onConnectClose(int code, String reason, boolean remote);
    }

    public WebSocketClient createConnect() throws URISyntaxException, IOException {
        return new WebSocketClient(new URI(Const.WEB_SOCKET_DY), new Draft_re()) {
            final DyRequest request = new DyRequest();
            final Thread heartBeatThread = new Thread(() -> {
                while (true) {
                    try {
                        send(request.heartBeat());
                        Thread.sleep(30000);        //心跳 斗鱼-45秒 虎牙-30秒
                    } catch (IOException | InterruptedException | WebsocketNotConnectedException e) {
                        e.printStackTrace();
                    }
                }
            });
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                try {
                    send(request.login(room.getRoomId()));      //发送登录请求
                    send(request.joinGroup(room.getRoomId()));  //发送加入群组请求
                    send(request.heartBeat());                  //发送心跳
                    heartBeatThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String message) {}

            @Override
            public void onMessage(ByteBuffer byteBuffer) {
                Charset charset = StandardCharsets.UTF_8;
                CharBuffer charBuffer = charset.decode(byteBuffer);
                String byteData = charBuffer.toString();
                DouYuDanMu parseDanMu = DanMuParserDY.parse(byteData);
                if(null != receiveListener
                        && null != parseDanMu) {
                    receiveListener.onReceiver(parseDanMu);
                }
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

    public static class DouYuDanMu {

        public DouYuDanMu(String type, long uid,
                          String nn, String txt, int level,
                          String ic, String bnn, int bl) {
            this.type = type;
            this.uid = uid;
            this.nn = nn;
            this.txt = txt;
            this.level = level;
            this.ic = ic;
            this.bnn = bnn;
            this.bl = bl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getNn() {
            return nn;
        }

        public void setNn(String nn) {
            this.nn = nn;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getIc() {
            return ic;
        }

        public void setIc(String ic) {
            this.ic = ic;
        }

        public String getBnn() {
            return bnn;
        }

        public void setBnn(String bnn) {
            this.bnn = bnn;
        }

        public int getBl() {
            return bl;
        }

        public void setBl(int bl) {
            this.bl = bl;
        }

        public boolean isBlackVip() {
            return blackVip;
        }

        public void setBlackVip(boolean blackVip) {
            this.blackVip = blackVip;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        boolean blackVip = false;   //拉黑
        boolean vip = false;        //重点关注对象

        String type;                //表示为“弹幕”消息，固定为 chatmsg
        String rid;                 //房间 id
        long uid;                   //发送者 uid
        String nn;	                //发送者昵称
        String txt;         	    //弹幕文本内容
        int level;	                //用户等级
        int gt;     	            //礼物头衔：默认值 0（表示没有头衔）
        int col;    	            //颜色：默认值 0（表示默认颜色弹幕）
        int ct;         	        //客户端类型：默认值 0
        int rg;	                    //房间权限组：默认值 1（表示普通权限用户）
        int pg;     	            //平台权限组：默认值 1（表示普通权限用户）
        int dlv;	                //酬勤等级：默认值 0（表示没有酬勤）
        int dc;	                    //酬勤数量：默认值 0（表示没有酬勤数量）
        int bdlv;	                //最高酬勤等级：默认值 0（表示全站都没有酬勤）
        int cmt;            	    //弹幕具体类型: 默认值 0（普通弹幕）
        int sahf;	                //扩展字段，一般不使用，可忽略
        String ic;	                //用户头像
        int nl;	                    //贵族等级
        int nc;	                    //贵族弹幕标识,0-非贵族弹幕,1-贵族弹幕,默认值 0
        long gatin; 	            //进入网关服务时间戳
        long chtin;	                //进入房间服务时间戳
        long repin;	                //进入发送服务时间戳
        String bnn;	                //徽章昵称
        int bl;	                    //徽章等级
        int brid;	                //徽章房间 id
        int hc;	                    //徽章信息校验码
        int ol; 	                //主播等级
        int rev;	                //是否反向弹幕标记: 0-普通弹幕，1-反向弹幕, 默认值 0
        int hl;	                    //是否高亮弹幕标记: 0-普通，1-高亮, 默认值 0
        int ifs;	                //是否粉丝弹幕标记: 0-非粉丝弹幕，1-粉丝弹幕, 默认值 0
        int p2p;                	//服务功能字段
        String[] el;    	        //用户获得的连击特效：数组类型，数组包含 eid（特效 id）,etp（特效类型）,sc（特效次数）信息，ef（特效标志）。
    }
}
