package com.lzm.lightLive.http.bean.dy

import com.google.gson.annotations.SerializedName

class DySortRoom {
    @SerializedName("pgcnt")
    var pageCount  = 0                          //数据量

    @SerializedName("rl")
    var relatedList : List<Related>? = null     //数据

    class Related {
        @SerializedName("rid")
        var roomId : Long = 0                   //房间号

        @SerializedName("uid")
        var uid : Long = 0                      //uid

        @SerializedName("rn")
        var roomName : String? = null           //房间名

        @SerializedName("nn")
        var nickName: String? = null            //昵称

        @SerializedName("ol")
        var online: Long = 0                   //在线

        @SerializedName("rs16")
        var thumb: String? = null              //预览图

        @SerializedName("av")
        var avatar: String? = null             //头像 https://apic.douyucdn.cn/upload/ + "av" + .jpg 如果是avatar_v3 + _middle.jpg

        @SerializedName("c2name")
        var cateName: String? = null           //分类名

        @SerializedName("desc")
        var desc: String? = null               //描述

        override fun toString(): String {
            return "Related{" +
                    "roomId=" + roomId +
                    ", uid=" + uid +
                    ", roomName='" + roomName + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", online=" + online +
                    ", thumb='" + thumb + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", cateName='" + cateName + '\'' +
                    ", desc='" + desc + '\'' +
                    '}'
        }
    }

    override fun toString(): String {
        return "HeatSortRoom{" +
                "pageCount=" + pageCount +
                ", relatedList=" + relatedList +
                '}'
    }
}