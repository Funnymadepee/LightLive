package com.lzm.lightLive.http.bean.bl

import com.google.gson.annotations.SerializedName

class BLRoom {

    companion object {
        lateinit var uid: String
    }

    /*room_id: 5194110,
short_id: 0,
uid: 5408366,
need_p2p: 0,
is_hidden: false,
is_locked: false,
is_portrait: false,
live_status: 1,
hidden_till: 0,
lock_till: 0,
encrypted: false,
pwd_verified: false,
live_time: 1664198054,
room_shield: 0,
is_sp: 0,
special_type: 0*/

    @SerializedName("room_id")
    var roomId: Long? = null                //房间号

    @SerializedName("short_id")
    var shortId: Int? = null                //?

    @SerializedName("uid")
    var uid: Long? = null                   //用户UID

    @SerializedName("need_p2p")
    var needP2p: Int? = 0                   //?

    @SerializedName("is_hidden")
    var isHidden: Boolean? = false          //是否隐藏?

    @SerializedName("is_locked")
    var isLocked: Boolean? = false          //是否锁定?

    @SerializedName("live_status")
    var liveStatus: Int? = 0                //直播状态 0-未开播 1-开播 2-轮播

    @SerializedName("pwd_verified")
    var pwdVerified: Boolean? = false       //密码验证?

    /*title: "猎杀爆杀！！",
room_id: 5194110,
uid: 5408366,
online: 161798,
live_time: 1664198054,
live_status: 1,
short_id: 0,
area: 3,
area_name: "网络游戏",
area_v2_id: 240,
area_v2_name: "APEX英雄",
area_v2_parent_name: "网游",
area_v2_parent_id: 2,
uname: "库库_sama",
face: "https://i2.hdslb.com/bfs/face/22c40d9f5569da64fc3a2a2c8e219fed38722c6d.jpg",
tag_name: "守望先锋,炉石传说,剑网3,dnf,最终幻想14,坦克世界",
tags: "吃鸡,APEX英雄,技术主播,钢枪主播,白给主播",
cover_from_user: "https://i0.hdslb.com/bfs/live/new_room_cover/901128619d9b8062e99c21af13e9cdd281a6274e.jpg",
keyframe: "https://i0.hdslb.com/bfs/live-key-frame/keyframe09262130000005194110tdc5bg.jpg",
lock_till: "0000-00-00 00:00:00",
hidden_till: "0000-00-00 00:00:00",
broadcast_type: 0*/

    @SerializedName("title")
    var title: String? = null               //标题

    @SerializedName("online")
    var online: Long? = null                //在线人数

    @SerializedName("live_time")
    var liveTime: Long? = null              //直播时长

    @SerializedName("area")
    var area: Int? = null                   //分区

    @SerializedName("area_name")
    var areaName: String? = null            //分区名

    @SerializedName("area_v2_id")
    var areaV2Id: Int? = null               //分区细分

    @SerializedName("area_v2_name")
    var areaV2Name: String? = null          //细分名

    @SerializedName("area_v2_parent_name")
    var areaV2ParentName: String? = null    //父分区名

    @SerializedName("uname")
    var nickName: String? = null            //昵称

    @SerializedName("face")
    var avatar: String? = null              //头像

    @SerializedName("tag_name")
    var tagName: String? = null             //标签

    @SerializedName("cover_from_user")
    var coverFromUser: String? = null       //用户封面

    @SerializedName("keyframe")
    var thumb: String? = null               //关键帧

    @SerializedName("broadcast_type")
    var broadCastType: Int? = null          //直播类型

    override fun toString(): String {
        return "BLRoom(" +
                "roomId=$roomId, " +
                "shortId=$shortId, " +
                "uid=$uid, " +
                "needP2p=$needP2p, " +
                "isHidden=$isHidden, " +
                "isLocked=$isLocked, " +
                "liveStatus=$liveStatus, " +
                "pwdVerified=$pwdVerified, " +
                "title=$title, " +
                "online=$online, " +
                "liveTime=$liveTime, " +
                "area=$area, " +
                "areaName=$areaName, " +
                "areaV2Id=$areaV2Id, " +
                "areaV2Name=$areaV2Name, " +
                "areaV2ParentName=$areaV2ParentName, " +
                "nickName=$nickName, " +
                "avatar=$avatar, " +
                "tagName=$tagName, " +
                "coverFromUser=$coverFromUser, " +
                "thumb=$thumb, " +
                "broadCastType=$broadCastType" +
                ")"
    }

    fun setUid(uid: String) {
//        val uid = uid
    }
}