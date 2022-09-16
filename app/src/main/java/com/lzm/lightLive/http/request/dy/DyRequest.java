package com.lzm.lightLive.http.request.dy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DyRequest implements IDyRequest {
    //登录请求
    @Override
    public byte[] login(String roomId) throws IOException {
        String message = "type@=loginreq/roomid@="+roomId+"/";
        return douyuRequestEncode(message);
    }
    //加入群组请求
    @Override
    public byte[] joinGroup(String roomId) throws IOException{
        String message ="type@=joingroup/rid@="+roomId+"/gid@=-9999/";
        return douyuRequestEncode(message);
    }
    //心跳
    @Override
    public byte[] heartBeat() throws IOException{
        String message = "type@=mrkl/";
        return douyuRequestEncode(message);
    }

    //将传入的数据变成符合斗鱼协议要求的字节流返回
    public byte[] douyuRequestEncode(String message) throws IOException {
        int dataLen1 = message.length() + 9;//4 字节小端整数，表示整条消息（包括自身）长度（字节数）。
        int dataLen2 = message.length() + 9;//消息长度出现两遍，二者相同。
        int send = 689;//689 客户端发送给弹幕服务器的文本格式数据,暂时未用，默认为 0。保留字段：暂时未用，默认为 0。
        byte[] msgBytes= message.getBytes(StandardCharsets.UTF_8);//斗鱼独创序列化文本数据，结尾必须为‘\0’。详细序列化、反序列化
        int end = 0;
        byte[] endBytes = new byte[1];
        endBytes[0] = (byte) (end  & 0xFF);;//结尾必须为‘\0’。详细序列化、反序列化

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(intToBytesLittle(dataLen1));
        bytes.write(intToBytesLittle(dataLen2));
        bytes.write(intToBytesLittle(send));
        bytes.write(msgBytes);
        bytes.write(endBytes);
        //返回byte[]
        return bytes.toByteArray();
    }

    //将整形转化为4位小端字节流
    public  byte[] intToBytesLittle(int value) {
        return new byte [] {
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };}
}
