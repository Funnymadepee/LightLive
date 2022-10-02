package com.lzm.lightLive.common

import com.lzm.lightLive.http.bean.Room

object Const {
    
    private val pair_xdd = Room("3168536", Room.LIVE_PLAT_DY)
    private val pair_mming = Room("6822146", Room.LIVE_PLAT_DY)
    private val pair_zpyan = Room("6204652", Room.LIVE_PLAT_DY)
    private val pair_mww = Room("4520630", Room.LIVE_PLAT_DY)
    private val pair_pdd = Room("101", Room.LIVE_PLAT_DY)
    private val pair_dsm = Room("606118", Room.LIVE_PLAT_DY)
    private val pair_zgg01 = Room("296059", Room.LIVE_PLAT_DY)
    private val pair_running = Room("2448877", Room.LIVE_PLAT_DY)
    private val pair_xiaohai = Room("701990", Room.LIVE_PLAT_DY)
    private val pair_tgltn = Room("27779514", Room.LIVE_PLAT_HY)
    private val pair_xxlu = Room("791133", Room.LIVE_PLAT_HY)
    private val pair_godv = Room("7911", Room.LIVE_PLAT_HY)
    private val pair_112 = Room("7911112", Room.LIVE_PLAT_HY)
    private val pair_pcl = Room("660004", Room.LIVE_PLAT_HY)
    private val pair_forever = Room("791166", Room.LIVE_PLAT_HY)
    private val pair_aluka = Room("364167", Room.LIVE_PLAT_HY)
    private val pair_longz = Room("791102", Room.LIVE_PLAT_HY)
    private val pair_ns12 = Room("791112", Room.LIVE_PLAT_HY)
    private val pair_998 = Room("998", Room.LIVE_PLAT_HY)
    private val pair_dy_pubg = Room("100", Room.LIVE_PLAT_DY)

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
            hosts.add(pair_mming)
            hosts.add(pair_zpyan)
            hosts.add(pair_mww)
            hosts.add(pair_zgg01)
            hosts.add(pair_running)
            hosts.add(pair_xiaohai)
            hosts.add(pair_tgltn)
            hosts.add(pair_xxlu)
            hosts.add(pair_112)
            hosts.add(pair_forever)
            hosts.add(pair_aluka)
            hosts.add(pair_longz)
            hosts.add(pair_ns12)
            hosts.add(pair_998)
            return hosts
        }
}