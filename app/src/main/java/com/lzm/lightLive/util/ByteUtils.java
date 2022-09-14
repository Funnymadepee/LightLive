package com.lzm.lightLive.util;

public class ByteUtils {

    /**
     * 以小端模式将int转成byte[]
     * @param value int形式的数据
     * @return 字节形式的数据
     */
    public static byte[] intToBytes(int value) {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };
    }

    /**
     * 以小端模式将byte[]转成int
     * @param src 字节形式的数组
     * @return int形式的数据
     */
    public static int bytesToInt(byte[] src) {
        return ((src[0] & 0xFF)
                | ((src[1] & 0xFF) << 8)
                | ((src[2] & 0xFF) << 16)
                | ((src[3] & 0xFF) << 24));
    }

}
