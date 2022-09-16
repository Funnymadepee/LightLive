package com.lzm.lightLive.http.bean.hy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProfileInfo implements Parcelable {

    @SerializedName("uid")
    long uid;                       //uid
    @SerializedName("yyid")
    long yyid;                      //?
    @SerializedName("nick")
    String nick;                    //昵称
    @SerializedName("avatar180")
    String avatar;                  //头像
    @SerializedName("activityId")
    int activityId;                 //？
    @SerializedName("activityCount")
    int activityCount;              //粉丝数
    @SerializedName("privateHost")
    String privateHost;             //？
    @SerializedName("profileRoom")
    long profileRoom;               //房间号

    protected ProfileInfo(Parcel in) {
        uid = in.readLong();
        yyid = in.readLong();
        nick = in.readString();
        avatar = in.readString();
        activityId = in.readInt();
        activityCount = in.readInt();
        privateHost = in.readString();
        profileRoom = in.readLong();
    }

    public static final Creator<ProfileInfo> CREATOR = new Creator<ProfileInfo>() {
        @Override
        public ProfileInfo createFromParcel(Parcel in) {
            return new ProfileInfo(in);
        }

        @Override
        public ProfileInfo[] newArray(int size) {
            return new ProfileInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(uid);
        dest.writeLong(yyid);
        dest.writeString(nick);
        dest.writeString(avatar);
        dest.writeInt(activityId);
        dest.writeInt(activityCount);
        dest.writeString(privateHost);
        dest.writeLong(profileRoom);
    }

    public ProfileInfo(long uid, long yyid, String nick, String avatar, int activityId, int activityCount, String privateHost, long profileRoom) {
        this.uid = uid;
        this.yyid = yyid;
        this.nick = nick;
        this.avatar = avatar;
        this.activityId = activityId;
        this.activityCount = activityCount;
        this.privateHost = privateHost;
        this.profileRoom = profileRoom;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getYyid() {
        return yyid;
    }

    public void setYyid(long yyid) {
        this.yyid = yyid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public String getPrivateHost() {
        return privateHost;
    }

    public void setPrivateHost(String privateHost) {
        this.privateHost = privateHost;
    }

    public long getProfileRoom() {
        return profileRoom;
    }

    public void setProfileRoom(long profileRoom) {
        this.profileRoom = profileRoom;
    }
}
