package com.lzm.lightLive.http.bean.hy

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class ProfileInfo : Parcelable {
    @SerializedName("uid")
    var uid: Long                       //uid

    @SerializedName("yyid")
    var yyid: Long                      //?

    @SerializedName("nick")
    var nick: String?                   //昵称

    @SerializedName("avatar180")
    var avatar: String?                 //头像

    @SerializedName("activityId")
    var activityId: Int                 //？

    @SerializedName("activityCount")
    var activityCount: Int              //粉丝数

    @SerializedName("privateHost")
    var privateHost: String?            //？

    @SerializedName("profileRoom")
    var profileRoom: Long                //房间号

    protected constructor(parcel: Parcel) {
        uid = parcel.readLong()
        yyid = parcel.readLong()
        nick = parcel.readString()
        avatar = parcel.readString()
        activityId = parcel.readInt()
        activityCount = parcel.readInt()
        privateHost = parcel.readString()
        profileRoom = parcel.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(uid)
        dest.writeLong(yyid)
        dest.writeString(nick)
        dest.writeString(avatar)
        dest.writeInt(activityId)
        dest.writeInt(activityCount)
        dest.writeString(privateHost)
        dest.writeLong(profileRoom)
    }

    constructor(
        uid: Long, yyid: Long, nick: String?,
        avatar: String?, activityId: Int, activityCount: Int,
        privateHost: String?, profileRoom: Long
    ) {
        this.uid = uid
        this.yyid = yyid
        this.nick = nick
        this.avatar = avatar
        this.activityId = activityId
        this.activityCount = activityCount
        this.privateHost = privateHost
        this.profileRoom = profileRoom
    }

    companion object CREATOR: Parcelable.Creator<ProfileInfo?> {
            override fun createFromParcel(parcel: Parcel): ProfileInfo {
                return ProfileInfo(parcel)
            }
            override fun newArray(size: Int): Array<ProfileInfo?> {
                return arrayOfNulls(size)
            }
    }
}