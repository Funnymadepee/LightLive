package com.lzm.lightLive.http.request.dy

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

class DyRequest {
    //登录请求
    @Throws(IOException::class)
    fun login(roomId: String?): ByteArray {
        val message = "type@=loginreq/roomid@=$roomId/"
        return dyRequestEncode(message)
    }

    //加入群组请求
    @Throws(IOException::class)
    fun joinGroup(roomId: String?): ByteArray {
        val message = "type@=joingroup/rid@=$roomId/gid@=-9999/"
        return dyRequestEncode(message)
    }

    //心跳
    @Throws(IOException::class)
    fun heartBeat(): ByteArray {
        val message = "type@=mrkl/"
        return dyRequestEncode(message)
    }

    //将传入的数据变成符合斗鱼协议要求的字节流返回
    @Throws(IOException::class)
    fun dyRequestEncode(message: String): ByteArray {
        //4 字节小端整数，表示整条消息（包括自身）长度（字节数）。
        val dataLen1 = message.length + 9

        //消息长度出现两遍，二者相同。
        val dataLen2 = message.length + 9

        //689 客户端发送给弹幕服务器的文本格式数据,暂时未用，默认为 0。保留字段：暂时未用，默认为 0。
        val send = 689

        //斗鱼独创序列化文本数据，结尾必须为‘\0’。详细序列化、反序列化
        val msgBytes = message.toByteArray(StandardCharsets.UTF_8)
        val end = 0
        val endBytes = ByteArray(1)

        //结尾必须为‘\0’。详细序列化、反序列化
        endBytes[0] = (end and 0xFF).toByte()
        val bytes = ByteArrayOutputStream()
        bytes.write(intToBytesLittle(dataLen1))
        bytes.write(intToBytesLittle(dataLen2))
        bytes.write(intToBytesLittle(send))
        bytes.write(msgBytes)
        bytes.write(endBytes)
        //返回byte[]
        return bytes.toByteArray()
    }

    //将整形转化为4位小端字节流
    fun intToBytesLittle(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            (value shr 8 and 0xFF).toByte(),
            (value shr 16 and 0xFF).toByte(),
            (value shr 24 and 0xFF).toByte()
        )
    }
}