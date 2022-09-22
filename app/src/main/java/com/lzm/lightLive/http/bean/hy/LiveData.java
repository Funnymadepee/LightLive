package com.lzm.lightLive.http.bean.hy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class LiveData implements Parcelable {
    @SerializedName("yyid")
    private long yyid;                                  //yyid?
    @SerializedName("channel")
    private long channel;                               //channel?
    @SerializedName("profileHomeHost")
    private String profileHomeHost;                     //profileHomeHost == String.valueOf(channel)
    @SerializedName("codecType")
    private int codecType;                              //codecType?
    @SerializedName("isBluRay")
    private int isBluRay;                               //isBluRay? 是否蓝光吧
    @SerializedName("level")
    private int level;                                  //等级
    @SerializedName("bluRayMBitRate")
    private String bluRayMBitRate;                      //蓝光等级
    @SerializedName("roomName")
    private String roomName;                            //房间标题
    @SerializedName("profileRoom")
    private long profileRoom;                           //房间号
    @SerializedName("userCount")
    private long userCount;                             //观众数
    @SerializedName("screenshot")
    private String screenshot;                          //预览图
    @SerializedName("gameFullName")
    private String gameFullName;                        //分类名
    @SerializedName("introduction")
    private String introduction;                        //直播介绍
    @SerializedName("sex")
    private int sex;                                    //性别 1--男 2--未知

    protected LiveData(Parcel in) {
        yyid = in.readLong();
        channel = in.readLong();
        profileHomeHost = in.readString();
        codecType = in.readInt();
        isBluRay = in.readInt();
        level = in.readInt();
        bluRayMBitRate = in.readString();
        roomName = in.readString();
        profileRoom = in.readLong();
        userCount = in.readLong();
        screenshot = in.readString();
        gameFullName = in.readString();
        introduction = in.readString();
        sex = in.readInt();
    }

    public static final Creator<LiveData> CREATOR = new Creator<LiveData>() {
        @Override
        public LiveData createFromParcel(Parcel in) {
            return new LiveData(in);
        }

        @Override
        public LiveData[] newArray(int size) {
            return new LiveData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(yyid);
        dest.writeLong(channel);
        dest.writeString(profileHomeHost);
        dest.writeInt(codecType);
        dest.writeInt(isBluRay);
        dest.writeInt(level);
        dest.writeString(bluRayMBitRate);
        dest.writeString(roomName);
        dest.writeLong(profileRoom);
        dest.writeLong(userCount);
        dest.writeString(screenshot);
        dest.writeString(gameFullName);
        dest.writeString(introduction);
        dest.writeInt(sex);
    }

    public LiveData(long yyid, long channel, String profileHomeHost,
                    int codecType, int isBluRay, int level,
                    String bluRayMBitRate, String roomName,
                    long profileRoom, long userCount,
                    String screenshot, String gameFullName,
                    String introduction, int sex) {
        this.yyid = yyid;
        this.channel = channel;
        this.profileHomeHost = profileHomeHost;
        this.codecType = codecType;
        this.isBluRay = isBluRay;
        this.level = level;
        this.bluRayMBitRate = bluRayMBitRate;
        this.roomName = roomName;
        this.profileRoom = profileRoom;
        this.userCount = userCount;
        this.screenshot = screenshot;
        this.gameFullName = gameFullName;
        this.introduction = introduction;
        this.sex = sex;
    }

    public long getYyid() {
        return yyid;
    }

    public void setYyid(long yyid) {
        this.yyid = yyid;
    }

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public String getProfileHomeHost() {
        return profileHomeHost;
    }

    public void setProfileHomeHost(String profileHomeHost) {
        this.profileHomeHost = profileHomeHost;
    }

    public int getCodecType() {
        return codecType;
    }

    public void setCodecType(int codecType) {
        this.codecType = codecType;
    }

    public int getIsBluRay() {
        return isBluRay;
    }

    public void setIsBluRay(int isBluRay) {
        this.isBluRay = isBluRay;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBluRayMBitRate() {
        return bluRayMBitRate;
    }

    public void setBluRayMBitRate(String bluRayMBitRate) {
        this.bluRayMBitRate = bluRayMBitRate;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getProfileRoom() {
        return profileRoom;
    }

    public void setProfileRoom(long profileRoom) {
        this.profileRoom = profileRoom;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getGameFullName() {
        return gameFullName;
    }

    public void setGameFullName(String gameFullName) {
        this.gameFullName = gameFullName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
