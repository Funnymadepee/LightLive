package com.lzm.lightLive.http.danmu.hy;

import com.google.gson.annotations.Expose;
import com.lzm.lightLive.http.danmu.basic.DanMuUserInfo;

public class HuYaUserInfo extends DanMuUserInfo {
    @Expose
    private int lUid = 0;
    @Expose
    private int lImid = 0;
    @Expose
    private int iGender = 0;
    @Expose
    private String sAvatarUrl = "";

    @Expose
    private int iNobleLevel = 0;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HuYaUserInfo{");
        sb.append("nickName=").append(getNickName());
//        sb.append(",lUid=").append(lUid);
//        sb.append(", lImid=").append(lImid);
//        sb.append(", iGender=").append(iGender);
//        sb.append(", sAvatarUrl='").append(sAvatarUrl).append('\'');
//        sb.append(", iNobleLevel=").append(iNobleLevel);
        sb.append('}');
        return sb.toString();
    }

    public int getlUid() {
        return lUid;
    }

    public void setlUid(int lUid) {
        this.lUid = lUid;
    }

    public int getlImid() {
        return lImid;
    }

    public void setlImid(int lImid) {
        this.lImid = lImid;
    }

    public int getiGender() {
        return iGender;
    }

    public void setiGender(int iGender) {
        this.iGender = iGender;
    }

    public String getsAvatarUrl() {
        return sAvatarUrl;
    }

    public void setsAvatarUrl(String sAvatarUrl) {
        this.sAvatarUrl = sAvatarUrl;
    }

    public int getiNobleLevel() {
        return iNobleLevel;
    }

    public void setiNobleLevel(int iNobleLevel) {
        this.iNobleLevel = iNobleLevel;
    }
}
