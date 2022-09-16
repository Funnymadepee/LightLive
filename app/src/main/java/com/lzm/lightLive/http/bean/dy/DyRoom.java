package com.lzm.lightLive.http.bean.dy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.lzm.lightLive.http.bean.Room;

public class DyRoom extends Room implements Parcelable {

    @SerializedName("cate_id")
    private String dyCateId;          //分类Id
    @SerializedName("start_time")
    private String dyStartTime;       //开播时间
    @SerializedName("online")
    private long dyOnline;            //？
    @SerializedName("owner_weight")
    private String dyOwnerWeight;     //？

    private String dyStreamUri;

    protected DyRoom(Parcel in) {
        super(in);
    }

    public String getDyCateId() {
        return dyCateId;
    }

    public void setDyCateId(String dyCateId) {
        this.dyCateId = dyCateId;
    }

    public String getDyStartTime() {
        return dyStartTime;
    }

    public void setDyStartTime(String dyStartTime) {
        this.dyStartTime = dyStartTime;
    }


    public long getDyOnline() {
        return dyOnline;
    }

    public void setDyOnline(long dyOnline) {
        this.dyOnline = dyOnline;
    }


    public String getDyOwnerWeight() {
        return dyOwnerWeight;
    }

    public void setDyOwnerWeight(String dyOwnerWeight) {
        this.dyOwnerWeight = dyOwnerWeight;
    }

    public String getDyStreamUri() {
        return dyStreamUri;
    }

    public void setDyStreamUri(String dyStreamUri) {
        setLiveStreamUri(dyStreamUri);
        this.dyStreamUri = dyStreamUri;
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
                ", liveStreamUri='" + liveStreamUri + '\'' +
                ", dyCateId='" + dyCateId + '\'' +
                ", dyStartTime='" + dyStartTime + '\'' +
                ", dyOnline=" + dyOnline +
                ", dyOwnerWeight='" + dyOwnerWeight + '\'' +
                ", dyStreamUri='" + dyStreamUri + '\'' +
                '}';
    }
}
