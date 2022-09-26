package com.lzm.lightLive.common

import com.lzm.lightLive.http.bean.Room

object Const {
    private val pair_pdd = Room("101", Room.LIVE_PLAT_DY)
    private val pair_dsm = Room("606118", Room.LIVE_PLAT_DY)
    private  val pair_dy_pubg = Room("100", Room.LIVE_PLAT_DY)
    private val pair_xdd = Room("3168536", Room.LIVE_PLAT_DY)
    private val pair_godv = Room("7911", Room.LIVE_PLAT_HY)
    private val pair_pcl = Room("660004", Room.LIVE_PLAT_HY)

    const val WEB_SOCKET_DY = "wss://danmuproxy.douyu.com:8506/"
    const val WEB_SOCKET_HY = "wss://cdnws.api.huya.com"

    var AVATAR_URL_DY = "https://apic.douyucdn.cn/upload/"

    val subscribeHost: List<Room>
        get() {
            val hosts: MutableList<Room> = ArrayList()
            hosts.add(pair_dy_pubg)
            hosts.add(pair_xdd)
            hosts.add(pair_pdd)
            hosts.add(pair_dsm)
            hosts.add(pair_godv)
            hosts.add(pair_pcl)
            return hosts
        }
}