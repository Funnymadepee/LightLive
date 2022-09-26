package com.lzm.lightLive.http.bean.hy

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class LiveData : Parcelable {
    @SerializedName("yyid")
    var yyid: Long                  //yyid?

    @SerializedName("channel")
    var channel: Long               //channel?

    @SerializedName("profileHomeHost")
    var profileHomeHost: String?    //profileHomeHost == String.valueOf(channel)

    @SerializedName("codecType")
    var codecType: Int              //codecType?

    @SerializedName("isBluRay")
    var isBluRay: Int               //isBluRay? 是否蓝光吧

    @SerializedName("level")
    var level: Int                  //等级

    @SerializedName("bluRayMBitRate")
    var bluRayMBitRate: String?     //蓝光码率

    @SerializedName("roomName")
    var roomName: String?           //房间标题


    @SerializedName("profileRoom")
    var profileRoom: Long           //房间号

    @SerializedName("userCount")
    var userCount: Long             //观众数

    @SerializedName("screenshot")
    var screenshot: String?         //预览图

    @SerializedName("gameFullName")
    var gameFullName: String?       //分类名

    @SerializedName("introduction")
    var introduction: String?       //直播介绍

    @SerializedName("sex")
    var sex: Int                    //性别 1--男 2--未知

    protected constructor(parcel: Parcel) {
        yyid = parcel.readLong()
        channel = parcel.readLong()
        profileHomeHost = parcel.readString()
        codecType = parcel.readInt()
        isBluRay = parcel.readInt()
        level = parcel.readInt()
        bluRayMBitRate = parcel.readString()
        roomName = parcel.readString()
        profileRoom = parcel.readLong()
        userCount = parcel.readLong()
        screenshot = parcel.readString()
        gameFullName = parcel.readString()
        introduction = parcel.readString()
        sex = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(yyid)
        dest.writeLong(channel)
        dest.writeString(profileHomeHost)
        dest.writeInt(codecType)
        dest.writeInt(isBluRay)
        dest.writeInt(level)
        dest.writeString(bluRayMBitRate)
        dest.writeString(roomName)
        dest.writeLong(profileRoom)
        dest.writeLong(userCount)
        dest.writeString(screenshot)
        dest.writeString(gameFullName)
        dest.writeString(introduction)
        dest.writeInt(sex)
    }

    constructor(
        yyid: Long, channel: Long, profileHomeHost: String?,
        codecType: Int, isBluRay: Int, level: Int,
        bluRayMBitRate: String?, roomName: String?,
        profileRoom: Long, userCount: Long,
        screenshot: String?, gameFullName: String?,
        introduction: String?, sex: Int
    ) {
        this.yyid = yyid
        this.channel = channel
        this.profileHomeHost = profileHomeHost
        this.codecType = codecType
        this.isBluRay = isBluRay
        this.level = level
        this.bluRayMBitRate = bluRayMBitRate
        this.roomName = roomName
        this.profileRoom = profileRoom
        this.userCount = userCount
        this.screenshot = screenshot
        this.gameFullName = gameFullName
        this.introduction = introduction
        this.sex = sex
    }

    companion object CREATOR : Parcelable.Creator<LiveData> {
        override fun createFromParcel(parcel: Parcel): LiveData {
            return LiveData(parcel)
        }

        override fun newArray(size: Int): Array<LiveData?> {
            return arrayOfNulls(size)
        }
    }
}