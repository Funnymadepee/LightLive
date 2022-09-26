package com.lzm.lightLive.http.danmu.hy

import com.google.gson.annotations.Expose
import com.lzm.lightLive.http.danmu.basic.DanMuUserInfo

class HuYaUserInfo : DanMuUserInfo() {
    @Expose
    var lUid = 0

    @Expose
    var lImid = 0

    @Expose
    var iGender = 0

    @Expose
    var sAvatarUrl = ""

    @Expose
    var iNobleLevel = 0

    override fun toString(): String {
        val sb = StringBuffer("HuYaUserInfo{")
        sb.append("nickName=").append(nickName)
//        sb.append(",lUid=").append(lUid);
//        sb.append(", lImid=").append(lImid);
//        sb.append(", iGender=").append(iGender);
//        sb.append(", sAvatarUrl='").append(sAvatarUrl).append('\'');
//        sb.append(", iNobleLevel=").append(iNobleLevel);
        sb.append('}')
        return sb.toString()
    }

}