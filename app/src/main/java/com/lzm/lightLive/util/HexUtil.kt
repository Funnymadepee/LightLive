package com.lzm.lightLive.util

import java.io.IOException
import kotlin.experimental.and
import kotlin.experimental.or

object HexUtil {
    /**
     * 以小端模式将int转成byte[]
     * @param value int形式的数据
     * @return 字节形式的数据
     */
    fun intToBytes(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            (value shr 8 and 0xFF).toByte(),
            (value shr 16 and 0xFF).toByte(),
            (value shr 24 and 0xFF).toByte()
        )
    }

    /**
     * 以小端模式将byte[]转成int
     * @param src 字节形式的数组
     * @return int形式的数据
     */
    fun bytesToInt(src: ByteArray): Int {
        return (src[0] and (0xFF).toByte()
                or (src[1] and (0xFF shl 8).toByte())
                or (src[2] and (0xFF shl 16).toByte())
                or (src[3] and (0xFF shl 24).toByte())).toInt()
    }

    @Throws(IOException::class)
    fun decodeHex(data: String): ByteArray {
        return decodeHex(data.toCharArray())
    }

    @Throws(IOException::class)
    fun decodeHex(data: CharArray): ByteArray {
        val out = ByteArray(data.size shr 1)
        decodeHex(data, out, 0)
        return out
    }

    @Throws(IOException::class)
    fun decodeHex(data: CharArray, out: ByteArray, outOffset: Int): Int {
        val len = data.size
        return if (len and 1 != 0) {
            throw IOException("Odd number of characters.")
        } else {
            val outLen = len shr 1
            if (out.size - outOffset < outLen) {
                throw IOException("Output array is not large enough to accommodate decoded data.")
            } else {
                var i = outOffset
                var j = 0
                while (j < len) {
                    var f = toDigit(data[j], j) shl 4
                    ++j
                    f = f or toDigit(data[j], j)
                    ++j
                    out[i] = (f and 255).toByte()
                    ++i
                }
                outLen
            }
        }
    }

    @Throws(IOException::class)
    internal fun toDigit(ch: Char, index: Int): Int {
        val digit = ch.digitToIntOrNull(16) ?: -1
        return if (digit == -1) {
            throw IOException("Illegal hexadecimal character $ch at index $index")
        } else {
            digit
        }
    }
}