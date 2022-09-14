package com.lzm.lightLive.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Room implements Parcelable {

    @SerializedName("room_id")
    private String roomId;          //房间号
    @SerializedName("room_thumb")
    private String roomThumb;       //房间预览图
    @SerializedName("cate_id")
    private String cateId;          //分类Id
    @SerializedName("cate_name")
    private String cateName;        //分类名
    @SerializedName("room_name")
    private String roomName;        //房间标题
    @SerializedName("room_status")
    private int roomStatus;         //开播状态 1--直播中; 0--未直播
    @SerializedName("start_time")
    private String startTime;       //开播时间
    @SerializedName("owner_name")
    private String ownerName;       //主播名
    @SerializedName("avatar")
    private String avatar;          //主播头像
    @SerializedName("online")
    private long online;          //？
    @SerializedName("hn")
    private int hn;                 //在线热度值
    @SerializedName("owner_weight")
    private String ownerWeight;     //？
    @SerializedName("fans_num")
    private String fansNum;         //粉丝数

    private String streamUri;

    public Room() {

    }

    public Room(String roomId, String roomThumb, String cateId,
                  String cateName, String roomName, int roomStatus,
                  String startTime, String ownerName, String avatar,
                  long online,int hn, String ownerWeight,
                  String fansNum) {
        this.roomId = roomId;
        this.roomThumb = roomThumb;
        this.cateId = cateId;
        this.cateName = cateName;
        this.roomName = roomName;
        this.roomStatus = roomStatus;
        this.startTime = startTime;
        this.ownerName = ownerName;
        this.avatar = avatar;
        this.online = online;
        this.hn = hn;
        this.ownerWeight = ownerWeight;
        this.fansNum = fansNum;
    }

    protected Room(Parcel in) {
        roomId = in.readString();
        roomThumb = in.readString();
        cateId = in.readString();
        cateName = in.readString();
        roomName = in.readString();
        roomStatus = in.readInt();
        startTime = in.readString();
        ownerName = in.readString();
        avatar = in.readString();
        online = in.readLong();
        hn = in.readInt();
        ownerWeight = in.readString();
        fansNum = in.readString();
        streamUri = in.readString();
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomThumb() {
        return roomThumb;
    }

    public void setRoomThumb(String roomThumb) {
        this.roomThumb = roomThumb;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getOnline() {
        return online;
    }

    public void setOnline(long online) {
        this.online = online;
    }

    public int getHn() {
        return hn;
    }

    public void setHn(int hn) {
        this.hn = hn;
    }

    public String getOwnerWeight() {
        return ownerWeight;
    }

    public void setOwnerWeight(String ownerWeight) {
        this.ownerWeight = ownerWeight;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomThumb='" + roomThumb + '\'' +
                ", cateId='" + cateId + '\'' +
                ", cateName='" + cateName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomStatus='" + roomStatus + '\'' +
                ", startTime='" + startTime + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", online='" + online + '\'' +
                ", hn='" + hn + '\'' +
                ", ownerWeight='" + ownerWeight + '\'' +
                ", fansNum='" + fansNum + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomId);
        dest.writeString(roomThumb);
        dest.writeString(cateId);
        dest.writeString(cateName);
        dest.writeString(roomName);
        dest.writeInt(roomStatus);
        dest.writeString(startTime);
        dest.writeString(ownerName);
        dest.writeString(avatar);
        dest.writeLong(online);
        dest.writeInt(hn);
        dest.writeString(ownerWeight);
        dest.writeString(fansNum);
        dest.writeString(streamUri);
    }
}
