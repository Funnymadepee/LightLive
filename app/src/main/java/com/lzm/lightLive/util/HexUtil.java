package com.lzm.lightLive.util;

import java.io.IOException;

public class HexUtil {

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

    public static byte[] decodeHex(String data) throws IOException {
        return decodeHex(data.toCharArray());
    }

    public static byte[] decodeHex(char[] data) throws IOException {
        byte[] out = new byte[data.length >> 1];
        decodeHex(data, out, 0);
        return out;
    }

    public static int decodeHex(char[] data, byte[] out, int outOffset) throws IOException {
        int len = data.length;
        if ((len & 1) != 0) {
            throw new IOException("Odd number of characters.");
        } else {
            int outLen = len >> 1;
            if (out.length - outOffset < outLen) {
                throw new IOException("Output array is not large enough to accommodate decoded data.");
            } else {
                int i = outOffset;

                for(int j = 0; j < len; ++i) {
                    int f = toDigit(data[j], j) << 4;
                    ++j;
                    f |= toDigit(data[j], j);
                    ++j;
                    out[i] = (byte)(f & 255);
                }

                return outLen;
            }
        }
    }

    protected static int toDigit(char ch, int index) throws IOException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IOException("Illegal hexadecimal character " + ch + " at index " + index);
        } else {
            return digit;
        }
    }

}
