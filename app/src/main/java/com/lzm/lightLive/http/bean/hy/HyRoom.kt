package com.lzm.lightLive.http.bean.hy

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lzm.lightLive.http.bean.Room

open class HyRoom protected constructor(`in`: Parcel) : Room(`in`), Parcelable {
    @SerializedName("liveStatus")
    protected var hyLiveStatus: String? = null  //直播状态 OFF = 未开播 ON = 直播中 REPLAY = 回放

    @SerializedName("realLiveStatus")
    var hyRealLiveStatus: String? = null        //实时直播

    @SerializedName("welcomeText")
    var hyWelcomeText: String? = null           //欢迎词？

    @SerializedName("isRoomPay")
    var isHyIsRoomPay= false                    //支付？

    @SerializedName("profileInfo")
    var hyProfileInfo: ProfileInfo? = null      //主播信息

    @SerializedName("liveData")
    var hyLiveData: LiveData? = null            //直播信息

    @SerializedName("stream")
    var hyStream: Stream? = null                //直播流信息

    override var platform: Int
        get() = LIVE_PLAT_HY
        set(platform) {
            super.platform = platform
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "HyRoom{" +
                "heatNum=" + heatNum +
                ", fansNum=" + fansNum +
                ", hostAvatar='" + hostAvatar + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", hostName='" + hostName + '\'' +
                ", streamStatus=" + streamStatus +
                ", liveStreamUri='" + liveStreamUriHigh + '\'' +
                ", hyLiveStatus='" + hyLiveStatus + '\'' +
                ", hyRealLiveStatus='" + hyRealLiveStatus + '\'' +
                ", hyWelcomeText='" + hyWelcomeText + '\'' +
                ", hyIsRoomPay=" + isHyIsRoomPay +
                ", hyProfileInfo=" + hyProfileInfo +
                ", hyLiveData=" + hyLiveData +
                ", hyStream=" + hyStream +
                '}'
    }

    companion object CREATOR : Parcelable.Creator<Room?> {
        override fun createFromParcel(parcel: Parcel): Room {
            return object : Room(parcel) {
                override var platform: Int
                    get() = LIVE_PLAT_HY
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