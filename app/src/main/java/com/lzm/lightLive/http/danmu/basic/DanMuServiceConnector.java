package com.lzm.lightLive.http.danmu.basic;

import android.util.Log;

import java.net.URI;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import com.lzm.lightLive.http.bean.Room;
import java.util.concurrent.ExecutorService;
import com.lzm.lightLive.http.request.Draft_re;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

public abstract class DanMuServiceConnector {

    protected final Room room;
    private ExecutorService executorService;
    private WebSocketClient webSocketClient;
    protected WebSocketListener listener;

    public DanMuServiceConnector(Room room, WebSocketListener listener) {
        this.room = room;
        this.listener = listener;
        executorService = Executors.newCachedThreadPool();
    }

    protected void start() {
        executorService.submit(()->{
            webSocketClient = create();
            webSocketClient.connect();
        });
    }

    protected void socketOnOpen(ServerHandshake handshake) {};
    protected void socketOnMessage(ByteBuffer byteBuffer) {};
    protected void socketOnClose(int code, String reason, boolean remote) {};
    protected void socketOnError(Exception ex) {};

    protected abstract int heartBeatInterval();
    protected abstract String getWebSocketUrl();
    protected abstract byte[] generateHeartBeatData() throws IOException;

    public WebSocketClient create() {
        return new WebSocketClient(URI.create(getWebSocketUrl()), new Draft_re()) {
            final Thread heartBeatThread = new Thread(() -> {
                while (true) {
                    try {
                        send(generateHeartBeatData());
                        Thread.sleep(heartBeatInterval());        //心跳 斗鱼-45秒 虎牙-30秒
                    } catch (InterruptedException | WebsocketNotConnectedException | IOException e) {
//                        e.printStackTrace();
                    }
                }
            });
            @Override
            public void onOpen(ServerHandshake handshake) {
                socketOnOpen(handshake);
                heartBeatThread.start();
            }

            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onMessage(ByteBuffer byteBuffer) {
                socketOnMessage(byteBuffer);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                socketOnClose(code, reason, remote);
            }

            @Override
            public void onError(Exception ex) {
                socketOnError(ex);
            }
        };
    }

    public void send(byte[] bytes) {
        if(null != webSocketClient) {
            webSocketClient.send(bytes);
        }
    }


    public void close() {
        if(null != webSocketClient) {
            webSocketClient.close();
        }
    }
}
