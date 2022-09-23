package com.lzm.lightLive.http.bean.dy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.lzm.lightLive.http.bean.Room;

public class DyRoom extends Room implements Parcelable {

    @SerializedName("cate_id")
    private String cateId;          //分类Id
    @SerializedName("start_time")
    private String startTime;       //开播时间
    @SerializedName("online")
    private long online;            //？
    @SerializedName("owner_weight")
    private String ownerWeight;     //？

    private String streamUri;

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in) {
                @Override
                public int getPlatform() {
                    return Room.LIVE_PLAT_DY;
                }
            };
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    protected DyRoom(Parcel in) {
        super(in);
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getOnline() {
        return online;
    }

    public void setOnline(long online) {
        this.online = online;
    }

    public String getOwnerWeight() {
        return ownerWeight;
    }

    public void setOwnerWeight(String ownerWeight) {
        this.ownerWeight = ownerWeight;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
    }

    @Override
    public int getPlatform() {
        return Room.LIVE_PLAT_DY;
    }

    @Override
    public String toString() {
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
                '}';
    }
}
