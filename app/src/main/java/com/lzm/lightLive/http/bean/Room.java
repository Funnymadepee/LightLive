package com.lzm.lightLive.http.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Room implements Parcelable {

    public static final int LIVE_STATUS_OFF = 0;            //未直播
    public static final int LIVE_STATUS_ON = 1;             //直播中
    public static final int LIVE_STATUS_REPLAY = 2;         //直播回放

    public static final int LIVE_PLAT_DY = 1;               //斗鱼
    public static final int LIVE_PLAT_HY = 2;               //虎牙
    public static final int LIVE_PLAT_BL = 3;               //哔哩哔哩
    public static final int LIVE_PLAT_UNDEFINED = 0;        //未定义

    @SerializedName(value = "hn", alternate = {"heatNum"})
    protected long heatNum;                         //热度
    @SerializedName(value = "fans_num", alternate = {"fansNum"})
    protected long fansNum;                         //粉丝数
    @SerializedName(value = "avatar", alternate = {"hostAvatar"})
    protected String hostAvatar;                    //头像
    @SerializedName(value = "room_id", alternate = {"roomId"})
    protected String roomId;                        //房间号
    @SerializedName(value = "room_name", alternate = {"roomName"})
    protected String roomName;                      //房间标题
    @SerializedName(value = "room_thumb", alternate = {"thumbUrl"})
    protected String thumbUrl;                      //预览图地址
    @SerializedName(value = "owner_name", alternate = {"hostName"})
    protected String hostName;                      //主播名称
    @SerializedName(value = "cate_name", alternate = {"cateName"})
    protected String cateName;                      //分类名称
    @SerializedName(value = "room_status", alternate = {"streamStatus"})
    protected int streamStatus;                     //直播状态

    protected String liveStreamUriHigh;             //直播流地址_高清
    protected String liveStreamUriLow;              //直播流地址_低清
    protected int platform;                         //平台

    public Room(long heatNum, int fansNum,
                String hostAvatar, String roomId,
                String roomName, String thumbUrl,
                String hostName, String liveStreamUriHigh, String liveStreamUriLow,
                String cateName, int liveStatus) {
        this.heatNum = heatNum;
        this.fansNum = fansNum;
        this.hostAvatar = hostAvatar;
        this.roomId = roomId;
        this.roomName = roomName;
        this.thumbUrl = thumbUrl;
        this.hostName = hostName;
        this.liveStreamUriHigh = liveStreamUriHigh;
        this.liveStreamUriLow = liveStreamUriLow;
        this.cateName = cateName;
        this.streamStatus = liveStatus;
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(heatNum);
        dest.writeLong(fansNum);
        dest.writeString(hostAvatar);
        dest.writeString(roomId);
        dest.writeString(roomName);
        dest.writeString(thumbUrl);
        dest.writeString(hostName);
        dest.writeString(liveStreamUriHigh);
        dest.writeString(liveStreamUriLow);
        dest.writeString(cateName);
        dest.writeInt(streamStatus);
        dest.writeInt(platform);
    }

    public Room(String roomId, int platform) {
        this.roomId = roomId;
        this.platform = platform;
    }

    public Room(Parcel in) {
        heatNum = in.readLong();
        fansNum = in.readLong();
        hostAvatar = in.readString();
        roomId = in.readString();
        roomName = in.readString();
        thumbUrl = in.readString();
        hostName = in.readString();
        liveStreamUriHigh = in.readString();
        liveStreamUriLow = in.readString();
        cateName = in.readString();
        streamStatus = in.readInt();
        platform = in.readInt();
    }


    public long getHeatNum() {
        return heatNum;
    }

    public void setHeatNum(long heatNum) {
        this.heatNum = heatNum;
    }

    public long getFansNum() {
        return fansNum;
    }

    public void setFansNum(long fansNum) {
        this.fansNum = fansNum;
    }

    public String getHostAvatar() {
        return hostAvatar;
    }

    public void setHostAvatar(String hostAvatar) {
        this.hostAvatar = hostAvatar;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLiveStreamUriHigh() {
        return liveStreamUriHigh;
    }

    public void setLiveStreamUriHigh(String liveStreamUriHigh) {
        this.liveStreamUriHigh = liveStreamUriHigh;
    }

    public String getLiveStreamUriLow() {
        return liveStreamUriLow;
    }

    public void setLiveStreamUriLow(String liveStreamUriLow) {
        this.liveStreamUriLow = liveStreamUriLow;
    }

    public int getStreamStatus() {
        return streamStatus;
    }

    public void setStreamStatus(int streamStatus) {
        this.streamStatus = streamStatus;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
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
                '}';
    }
}
