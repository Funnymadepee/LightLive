package com.lzm.lightLive.http.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class Room : Parcelable {
    @SerializedName(value = "hn", alternate = ["heatNum"])
    var heatNum: Long = 0                   //热度

    @SerializedName(value = "fans_num", alternate = ["fansNum"])
    var fansNum: Long = 0                   //粉丝数

    @SerializedName(value = "avatar", alternate = ["hostAvatar"])
    var hostAvatar: String? = null          //头像

    @SerializedName(value = "room_id", alternate = ["roomId"])
    var roomId: String?                     //房间号

    @SerializedName(value = "room_name", alternate = ["roomName"])
    var roomName: String? = null            //房间标题

    @SerializedName(value = "room_thumb", alternate = ["thumbUrl"])
    var thumbUrl: String? = null            //预览图地址

    @SerializedName(value = "owner_name", alternate = ["hostName"])
    var hostName: String? = null            //主播名称

    @SerializedName(value = "cate_name", alternate = ["cateName"])
    var cateName: String? = null            //分类名称

    @SerializedName(value = "room_status", alternate = ["streamStatus"])
    var streamStatus = 0                    //直播状态

    var liveStreamUriHigh: String? = null   //直播流地址_高清

    var liveStreamUriLow: String? = null    //直播流地址_低清

    open var platform = LIVE_PLAT_UNDEFINED      //平台

    constructor(
        heatNum: Long, fansNum: Int,
        hostAvatar: String?, roomId: String?,
        roomName: String?, thumbUrl: String?,
        hostName: String?, liveStreamUriHigh: String?, liveStreamUriLow: String?,
        cateName: String?, liveStatus: Int
    ) {
        this.heatNum = heatNum
        this.fansNum = fansNum.toLong()
        this.hostAvatar = hostAvatar
        this.roomId = roomId
        this.roomName = roomName
        this.thumbUrl = thumbUrl
        this.hostName = hostName
        this.liveStreamUriHigh = liveStreamUriHigh
        this.liveStreamUriLow = liveStreamUriLow
        this.cateName = cateName
        streamStatus = liveStatus
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(heatNum)
        dest.writeLong(fansNum)
        dest.writeString(hostAvatar)
        dest.writeString(roomId)
        dest.writeString(roomName)
        dest.writeString(thumbUrl)
        dest.writeString(hostName)
        dest.writeString(liveStreamUriHigh)
        dest.writeString(liveStreamUriLow)
        dest.writeString(cateName)
        dest.writeInt(streamStatus)
        dest.writeInt(platform)
    }

    constructor(roomId: String?, platform: Int) {
        this.roomId = roomId
        this.platform = platform
    }

    constructor(parcel: Parcel) {
        heatNum = parcel.readLong()
        fansNum = parcel.readLong()
        hostAvatar = parcel.readString()
        roomId = parcel.readString()
        roomName = parcel.readString()
        thumbUrl = parcel.readString()
        hostName = parcel.readString()
        liveStreamUriHigh = parcel.readString()
        liveStreamUriLow = parcel.readString()
        cateName = parcel.readString()
        streamStatus = parcel.readInt()
        platform = parcel.readInt()
    }

    override fun toString(): String {
        return "Room{" +
                "heatNum=" + heatNum +
                ", fansNum=" + fansNum +
                ", hostAvatar='" + hostAvatar + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", hostName='" + hostName + '\'' +
                ", cateName='" + cateName + '\'' +
                ", streamStatus=" + streamStatus +
                ", liveStreamUriHigh='" + liveStreamUriHigh + '\'' +
                ", liveStreamUriLow='" + liveStreamUriLow + '\'' +
                ", platform=" + platform +
                '}'
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        const val LIVE_STATUS_OFF = 0       //未直播
        const val LIVE_STATUS_ON = 1        //直播中
        const val LIVE_STATUS_REPLAY = 2    //直播回放
        const val LIVE_PLAT_DY = 1          //斗鱼
        const val LIVE_PLAT_HY = 2          //虎牙
        const val LIVE_PLAT_BL = 3          //哔哩哔哩
        const val LIVE_PLAT_UNDEFINED = 0   //未定义

        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}