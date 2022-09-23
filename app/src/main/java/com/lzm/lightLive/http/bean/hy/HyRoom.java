package com.lzm.lightLive.http.bean.hy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.lzm.lightLive.http.bean.Room;

public class HyRoom extends Room implements Parcelable{

    @SerializedName("liveStatus")
    protected String hyLiveStatus;            //直播状态 OFF = 未开播 ON = 直播中 REPLAY = 回放

    @SerializedName("realLiveStatus")
    protected String hyRealLiveStatus;        //实时直播

    @SerializedName("welcomeText")
    protected String hyWelcomeText;           //欢迎词？

    @SerializedName("isRoomPay")
    protected boolean hyIsRoomPay;            //支付？

    @SerializedName("profileInfo")
    protected ProfileInfo hyProfileInfo;      //主播信息

    @SerializedName("liveData")
    protected LiveData hyLiveData;            //直播信息

    @SerializedName("stream")
    protected Stream hyStream;                //直播流信息

    protected HyRoom(Parcel in) {
        super(in);
    }

    @Override
    public int getPlatform() {
        return Room.LIVE_PLAT_HY;
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in) {
                @Override
                public int getPlatform() {
                    return Room.LIVE_PLAT_HY;
                }
            };
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

    public void setHyLiveStatus(String hyLiveStatus) {
        this.hyLiveStatus = hyLiveStatus;
    }

    public String getHyRealLiveStatus() {
        return hyRealLiveStatus;
    }

    public void setHyRealLiveStatus(String hyRealLiveStatus) {
        this.hyRealLiveStatus = hyRealLiveStatus;
    }

    public String getHyWelcomeText() {
        return hyWelcomeText;
    }

    public void setHyWelcomeText(String hyWelcomeText) {
        this.hyWelcomeText = hyWelcomeText;
    }

    public boolean isHyIsRoomPay() {
        return hyIsRoomPay;
    }

    public void setHyIsRoomPay(boolean hyIsRoomPay) {
        this.hyIsRoomPay = hyIsRoomPay;
    }

    public ProfileInfo getHyProfileInfo() {
        return hyProfileInfo;
    }

    public void setHyProfileInfo(ProfileInfo hyProfileInfo) {
        this.hyProfileInfo = hyProfileInfo;
    }

    public LiveData getHyLiveData() {
        return hyLiveData;
    }

    public void setHyLiveData(LiveData hyLiveData) {
        this.hyLiveData = hyLiveData;
    }

    public Stream getHyStream() {
        return hyStream;
    }

    public void setHyStream(Stream hyStream) {
        this.hyStream = hyStream;
    }

    @Override
    public String toString() {
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
                ", hyIsRoomPay=" + hyIsRoomPay +
                ", hyProfileInfo=" + hyProfileInfo +
                ", hyLiveData=" + hyLiveData +
                ", hyStream=" + hyStream +
                '}';
    }
}
