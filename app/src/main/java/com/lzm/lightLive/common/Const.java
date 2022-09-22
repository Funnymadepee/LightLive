package com.lzm.lightLive.common;

import android.util.Pair;

import com.lzm.lightLive.http.bean.Room;

import java.util.ArrayList;
import java.util.List;

public class Const {

    public static final Room pair_xdd = new Room("3168536", Room.LIVE_PLAT_DY);
    public static final Room pair_mming = new Room("6822146", Room.LIVE_PLAT_DY);
    public static final Room pair_zpyan = new Room("6204652", Room.LIVE_PLAT_DY);
    public static final Room pair_mww = new Room("4520630", Room.LIVE_PLAT_DY);
    public static final Room pair_pdd = new Room("101", Room.LIVE_PLAT_DY);
    public static final Room pair_dsm = new Room("606118", Room.LIVE_PLAT_DY);
    public static final Room pair_zgg01 = new Room("296059", Room.LIVE_PLAT_DY);
    public static final Room pair_qqq = new Room("19223", Room.LIVE_PLAT_DY);
    public static final Room pair_running = new Room("2448877", Room.LIVE_PLAT_DY);
    public static final Room pair_xiaohai = new Room("701990", Room.LIVE_PLAT_DY);
    public static final Room pair_tgltn = new Room("27779514", Room.LIVE_PLAT_HY);
    public static final Room pair_xxlu = new Room("791133", Room.LIVE_PLAT_HY);
    public static final Room pair_godv = new Room("7911", Room.LIVE_PLAT_HY);
    public static final Room pair_112 = new Room("7911112", Room.LIVE_PLAT_HY);
    public static final Room pair_pcl = new Room("660004", Room.LIVE_PLAT_HY);
    public static final Room pair_ibiza = new Room("340358", Room.LIVE_PLAT_HY);
    public static final Room pair_forever = new Room("791166", Room.LIVE_PLAT_HY);
    public static final Room pair_aluka = new Room("364167", Room.LIVE_PLAT_HY);
    public static final Room pair_longz = new Room("791102", Room.LIVE_PLAT_HY);
    public static final Room pair_ns12 = new Room("791112", Room.LIVE_PLAT_HY);
    public static final Room pair_998 = new Room("998", Room.LIVE_PLAT_HY);

    public static final String WEB_SOCKET_DY = "wss://danmuproxy.douyu.com:8506/";

    public static final String WEB_SOCKET_HY = "wss://cdnws.api.huya.com";

    public static List<Room> getSubscribeHost() {
        List<Room> hosts = new ArrayList<>();
        hosts.add(pair_mming);
        hosts.add(pair_zgg01);
        hosts.add(pair_running);
        hosts.add(pair_mww);
        hosts.add(pair_xdd);
        hosts.add(pair_qqq);
        hosts.add(pair_pdd);
        hosts.add(pair_dsm);
        hosts.add(pair_xiaohai);
        hosts.add(pair_zpyan);
        hosts.add(pair_tgltn);
        hosts.add(pair_xxlu);
        hosts.add(pair_godv);
        hosts.add(pair_112);
        hosts.add(pair_pcl);
        hosts.add(pair_ibiza);
        hosts.add(pair_forever);
        hosts.add(pair_aluka);
        hosts.add(pair_longz);
        hosts.add(pair_ns12);
        hosts.add(pair_998);
        return hosts;
    }

}
