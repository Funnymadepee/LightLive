package com.lzm.lightLive.http.bean.dy

import com.google.gson.annotations.SerializedName
import java.util.*

class DyStream(
    @field:SerializedName("room_id") var roomId: String,
    @field:SerializedName("rtmp_cdn") var rtmpCdn: String,
    @field:SerializedName("rtmp_url") var rtmpUrl: String,
    @field:SerializedName("rtmp_live") var rtmpLive: String,
    @field:SerializedName("client_ip") var clientIp: String,
    @field:SerializedName("multirates") var multiRates: Array<String>,
    @field:SerializedName("is_pass_player") var is_pass_player: Int,
    @field:SerializedName("rate") var rate: Int,
    @field:SerializedName("is_mixed") var is_mixed: Boolean,
    @field:SerializedName("mixed_url") var mixed_url: String,
    @field:SerializedName("streamStatus") var streamStatus: Int,
    @field:SerializedName("online") var online: Int
) {

    override fun toString(): String {
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
                '}'
    }
}