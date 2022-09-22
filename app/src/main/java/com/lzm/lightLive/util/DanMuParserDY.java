package com.lzm.lightLive.util;

import android.util.Log;

import com.lzm.lightLive.http.request.dy.DyDanMuConnect;

public class DanMuParserDY {

    public static DyDanMuConnect.DouYuDanMu parse(String message) {
        if(message.contains("type@=chatmsg")) {
            String uid = message.substring(message.indexOf("/uid@="), message.indexOf("/nn@="))
                    .replaceAll("/uid@=", "");
            String nickName =
                    message.substring(message.indexOf("/nn@="), message.indexOf("/txt"))
                            .replaceAll("/nn@=", "");

            int level = Integer.parseInt(message.substring(message.indexOf("/level@="), message.indexOf("/sahf@="))
                    .replaceAll("/level@=", ""));
            String badge = message.substring(message.indexOf("/bnn@="), message.indexOf("/bl@="))
                    .replaceAll("/bnn@=", "");
            String badgeLevel = message.substring(message.indexOf("/bl@="), message.indexOf("/brid@="))
                    .replaceAll("/bl@=", "");
            String content = message.substring(message.indexOf("txt@="), message.indexOf("/cid"))
                    .replaceAll("txt@=", "");
//            content += "0123456789 0123456789 0123456789 0123456789";
            String avatar = message.substring(message.indexOf("/ic@="), message.indexOf("/level@="))
                    .replaceAll("/ic@=", "");

            DyDanMuConnect.DouYuDanMu dyDanMu = new DyDanMuConnect.DouYuDanMu("chatmsg", Long.parseLong(uid),
                    nickName, content, level, avatar, badge, Integer.parseInt(badgeLevel));

            //TODO 根据uid / nickName 查找黑名单 / vip重点关注用户
            if (nickName.equals("realzmzzZ")) {
                Log.e("TAG", "parse: " + dyDanMu.getUid() );
                dyDanMu.setVip(true);
            }
            return dyDanMu;
        }else {
            return null;
        }
    }

}
