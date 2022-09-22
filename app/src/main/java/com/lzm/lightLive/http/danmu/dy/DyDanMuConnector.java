package com.lzm.lightLive.http.danmu.dy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import com.lzm.lightLive.common.Const;
import com.lzm.lightLive.http.bean.Room;
import java.nio.charset.StandardCharsets;
import com.lzm.lightLive.util.DanMuParserDY;
import com.lzm.lightLive.http.danmu.basic.DanMu;
import com.lzm.lightLive.http.request.dy.DyRequest;
import org.java_websocket.handshake.ServerHandshake;
import com.lzm.lightLive.http.danmu.basic.WebSocketListener;
import com.lzm.lightLive.http.danmu.basic.DanMuServiceConnector;

public class DyDanMuConnector extends DanMuServiceConnector {

    private final DyRequest request;

    public DyDanMuConnector(Room room, WebSocketListener listener) {
        super(room, listener);
        request = new DyRequest();
        start();
    }

    @Override
    public String getWebSocketUrl() {
        return Const.WEB_SOCKET_DY;
    }

    @Override
    public byte[] generateHeartBeatData() throws IOException {
        return request.heartBeat();
    }

    @Override
    public int heartBeatInterval() {
        return 4500;
    }

    @Override
    protected void socketOnOpen(ServerHandshake handshake) {
        super.socketOnOpen(handshake);
        try {
            send(request.login(room.getRoomId()));      //发送登录请求
            send(request.joinGroup(room.getRoomId()));  //发送加入群组请求
            send(request.heartBeat());                  //发送心跳
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void socketOnMessage(ByteBuffer byteBuffer) {
        super.socketOnMessage(byteBuffer);
        Charset charset = StandardCharsets.UTF_8;
        CharBuffer charBuffer = charset.decode(byteBuffer);
        String byteData = charBuffer.toString();
        DanMu parseDanMu = DanMuParserDY.parse(byteData);
        if(null != listener
                && null != parseDanMu) {
            listener.onMessage(parseDanMu);
        }
    }

}
