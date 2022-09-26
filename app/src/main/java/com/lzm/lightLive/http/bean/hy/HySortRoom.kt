package com.lzm.lightLive.http.bean.hy

import com.google.gson.annotations.SerializedName

class HySortRoom {
    var status = 0
    var message: String? = null
    var data: RecommendData? = null

    class RecommendData {
        var page = 0
        var pageSize = 0
        var totalPage = 0
        var totalCount = 0

        @SerializedName("datas")
        var datas: List<HyRecommendRoom>? = null
        override fun toString(): String {
            return "HyHeatSortRoom{" +
                    "page=" + page +
                    ", pageSize=" + pageSize +
                    ", totalPage=" + totalPage +
                    ", totalCount=" + totalCount +
                    ", datas=" + datas +
                    '}'
        }
    }

    class HyRecommendRoom {
        @SerializedName("gameFullName")
        var cateName: String? = null

        @SerializedName("totalCount")
        var heatNum: String? = null

        @SerializedName("roomName")
        var roomName: String? = null

        @SerializedName("nick")
        var hostName: String? = null

        @SerializedName("screenshot")
        var thumbUrl: String? = null

        @SerializedName("avatar180")
        var avatar: String? = null

        @SerializedName("introduction")
        var desc: String? = null

        @SerializedName("recommendTagName")
        var tagName: String? = null

        @SerializedName("profileRoom")
        var roomId: String? = null

        @SerializedName("isRoomPay")
        var liveStatus: String? = null
        override fun toString(): String {
            return "HyRecommendRoom{" +
                    "cateName='" + cateName + '\'' +
                    ", heatNum='" + heatNum + '\'' +
                    ", roomName='" + roomName + '\'' +
                    ", hostName='" + hostName + '\'' +
                    ", thumbUrl='" + thumbUrl + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", desc='" + desc + '\'' +
                    ", tagName='" + tagName + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", liveStatus='" + liveStatus + '\'' +
                    '}'
        }
    }

    override fun toString(): String {
        return "HyHeatSortRoom{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }
}