package com.lzm.lightLive.http.danmu.basic;

public class DanMuUserInfo {

    private int level;
    private String uid;
    private String badge;
    private String avatar;
    private String nickName;
    private String badgeLevel;
    private boolean isVip = false;
    private boolean isBlackList = false;


    public DanMuUserInfo() {
    }

    public DanMuUserInfo(String nickName) {
        this.nickName = nickName;
    }

    public DanMuUserInfo(int level, String uid, String badge, String avatar, String nickName, String badgeLevel) {
        this.level = level;
        this.uid = uid;
        this.badge = badge;
        this.avatar = avatar;
        this.nickName = nickName;
        this.badgeLevel = badgeLevel;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBadgeLevel() {
        return badgeLevel;
    }

    public void setBadgeLevel(String badgeLevel) {
        this.badgeLevel = badgeLevel;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isBlackList() {
        return isBlackList;
    }

    public void setBlackList(boolean blackList) {
        isBlackList = blackList;
    }

    @Override
    public String toString() {
        return "DanMuUserInfo{" +
                "level=" + level +
                ", uid='" + uid + '\'' +
                ", badge='" + badge + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isVip=" + isVip +
                ", nickName='" + nickName + '\'' +
                ", badgeLevel='" + badgeLevel + '\'' +
                ", isBlackList=" + isBlackList +
                '}';
    }
}
