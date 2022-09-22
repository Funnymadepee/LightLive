package com.lzm.lightLive.http.danmu.hy;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;

public class HuYaDanMuUserDataTarsBase extends TarsStructBase {
    private HuYaUserInfo huYaUserInfo = null;
    @Override
    public void writeTo(TarsOutputStream os) {

    }

    @Override
    public void readFrom(TarsInputStream is) {
        //解析参数来源：https://github.com/759434091/danmu-crawler/blob/master/huya/pojo/Huya.js
        is.setServerEncoding("utf-8");
        huYaUserInfo = new HuYaUserInfo();
        //读此参数时无数据报错，故取消
//        huYaUserInfo.setlUid(is.read(0, 0, false));
        huYaUserInfo.setlImid(is.read(0, 1, false));
        huYaUserInfo.setNickName(is.read("", 2, false));
        huYaUserInfo.setiGender(is.read(0, 3, false));
        huYaUserInfo.setsAvatarUrl(is.read("", 4, false));
        huYaUserInfo.setiNobleLevel(is.read(0, 5, false));

    }

    public HuYaUserInfo getHuYaUserInfo() {
        return huYaUserInfo;
    }
}