package com.lzm.lightLive.http.bean.dy

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lzm.lightLive.http.bean.Room

open class DyRoom protected constructor(`in`: Parcel) : Room(`in`), Parcelable {
    @SerializedName("cate_id")
    var cateId: String? = null           //分类Id

    @SerializedName("start_time")
    var startTime : String? = null      //开播时间

    @SerializedName("online")
    var online : Long = 0               //？

    @SerializedName("owner_weight")
    var ownerWeight : String? = null    //？

    private var streamUri: String? = null

    override var platform: Int
        get() = LIVE_PLAT_DY
        set(platform) {
            super.platform = platform
        }

    override fun toString(): String {
        return "DyRoom{" +
                "heatNum=" + heatNum +
                ", fansNum=" + fansNum +
                ", hostAvatar='" + hostAvatar + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", hostName='" + hostName + '\'' +
                ", cateName='" + cateName + '\'' +
                ", streamStatus=" + streamStatus +
                ", liveStreamUri='" + liveStreamUriHigh + '\'' +
                ", cateId='" + cateId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", online=" + online +
                ", ownerWeight='" + ownerWeight + '\'' +
                ", streamUri='" + streamUri + '\'' +
                '}'
    }

    companion object CREATOR : Parcelable.Creator<Room?> {
        override fun createFromParcel(parcel: Parcel): Room {
            return object : Room(parcel) {
                override var platform: Int
                    get() = LIVE_PLAT_DY
                    set(platform) {
                        super.platform = platform
                    }
            }
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }

}