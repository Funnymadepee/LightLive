package com.lzm.lightLive.http.bean.dy;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class DyStream {

    @SerializedName("room_id")
    String roomId;

    @SerializedName("rtmp_cdn")
    String rtmpCdn;

    @SerializedName("rtmp_url")
    String rtmpUrl;

    @SerializedName("rtmp_live")
    String rtmpLive;

    @SerializedName("client_ip")
    String clientIp;

    @SerializedName("multirates")
    String[] multiRates;

    @SerializedName("is_pass_player")
    int is_pass_player;

    @SerializedName("rate")
    int rate;

    @SerializedName("is_mixed")
    boolean is_mixed;

    @SerializedName("mixed_url")
    String mixed_url;

    @SerializedName("streamStatus")
    int streamStatus;

    @SerializedName("online")
    int online;

    public DyStream(String roomId, String rtmpCdn
            , String rtmpUrl, String rtmpLive, String clientIp
            , String[] multiRates, int is_pass_player, int rate
            , boolean is_mixed, String mixed_url, int streamStatus, int online) {
        this.roomId = roomId;
        this.rtmpCdn = rtmpCdn;
        this.rtmpUrl = rtmpUrl;
        this.rtmpLive = rtmpLive;
        this.clientIp = clientIp;
        this.multiRates = multiRates;
        this.is_pass_player = is_pass_player;
        this.rate = rate;
        this.is_mixed = is_mixed;
        this.mixed_url = mixed_url;
        this.streamStatus = streamStatus;
        this.online = online;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRtmpCdn() {
        return rtmpCdn;
    }

    public void setRtmpCdn(String rtmpCdn) {
        this.rtmpCdn = rtmpCdn;
    }

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public String getRtmpLive() {
        return rtmpLive;
    }

    public void setRtmpLive(String rtmpLive) {
        this.rtmpLive = rtmpLive;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String[] getMultiRates() {
        return multiRates;
    }

    public void setMultiRates(String[] multiRates) {
        this.multiRates = multiRates;
    }

    public int getIs_pass_player() {
        return is_pass_player;
    }

    public void setIs_pass_player(int is_pass_player) {
        this.is_pass_player = is_pass_player;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean getIs_mixed() {
        return is_mixed;
    }

    public void setIs_mixed(boolean is_mixed) {
        this.is_mixed = is_mixed;
    }

    public String getMixed_url() {
        return mixed_url;
    }

    public void setMixed_url(String mixed_url) {
        this.mixed_url = mixed_url;
    }

    public int getStreamStatus() {
        return streamStatus;
    }

    public void setStreamStatus(int streamStatus) {
        this.streamStatus = streamStatus;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "DyStream{" +
                "roomId='" + roomId + '\'' +
                ", rtmpCdn='" + rtmpCdn + '\'' +
                ", rtmpUrl='" + rtmpUrl + '\'' +
                ", rtmpLive='" + rtmpLive + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", multiRates=" + Arrays.toString(multiRates) +
                ", is_pass_player=" + is_pass_player +
                ", rate=" + rate +
                ", is_mixed=" + is_mixed +
                ", mixed_url='" + mixed_url + '\'' +
                ", streamStatus=" + streamStatus +
                ", online=" + online +
                '}';
    }
}
