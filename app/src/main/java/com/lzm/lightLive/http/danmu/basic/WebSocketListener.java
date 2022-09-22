package com.lzm.lightLive.http.danmu.basic;

import org.java_websocket.handshake.ServerHandshake;
import java.nio.ByteBuffer;

public abstract class WebSocketListener {

    public void onError(Exception e) {}
    public void onMessage(String message) {}
    public void onMessage(ByteBuffer byteBuffer) {}
    public void onMessage(DanMu danMu) {}
    public void onOpen(ServerHandshake handshake) {}
    public void onClose(int code, String reason, boolean remote) {}

}
