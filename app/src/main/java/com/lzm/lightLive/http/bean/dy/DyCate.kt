package com.lzm.lightLive.http.bean.dy

import com.google.gson.annotations.SerializedName

class DyCate {
    @SerializedName("error")
    var error = 0

    @SerializedName("msg")
    var message: String? = null

    @SerializedName("data")
    var data: List<DyCateData>? = null

    class DyCateData {
        @SerializedName("cate2Id")
        var cateId = 0

        @SerializedName("cate2Name")
        var cateName: String? = null

        @SerializedName("cate2Icon")
        var cateIcon: String? = null

        @SerializedName("cate2Url")
        var cateUrl: String? = null

        @SerializedName("hot")
        var cateHeat: Long = 0
        override fun toString(): String {
            return "DyCate{" +
                    "cateId=" + cateId +
                    ", cateName='" + cateName + '\'' +
                    ", cateIcon='" + cateIcon + '\'' +
                    ", cateUrl='" + cateUrl + '\'' +
                    ", cateHeat=" + cateHeat +
                    '}'
        }
    }

    override fun toString(): String {
        return "DyCate{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }
}