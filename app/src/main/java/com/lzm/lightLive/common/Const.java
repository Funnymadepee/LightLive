package com.lzm.lightLive.common;

import android.util.Pair;

import com.lzm.lightLive.http.bean.Room;

import java.util.ArrayList;
import java.util.List;

public class Const {

    public static final Pair<String, Integer> pair_xdd = new Pair<>("3168536", Room.LIVE_PLAT_DY);
    public static final Pair<String, Integer> pair_mming = new Pair<>("6822146", Room.LIVE_PLAT_DY);
    public static final Pair<String, Integer> pair_zgg01 = new Pair<>("296059", Room.LIVE_PLAT_DY);
    public static final Pair<String, Integer> pair_qqq = new Pair<>("19223", Room.LIVE_PLAT_DY);
    public static final Pair<String, Integer> pair_running = new Pair<>("2448877", Room.LIVE_PLAT_DY);
    public static final Pair<String, Integer> pair_tgltn = new Pair<>("27779514", Room.LIVE_PLAT_HY);
    public static final Pair<String, Integer> pair_xxlu = new Pair<>("791133", Room.LIVE_PLAT_HY);
    public static final Pair<String, Integer> pair_godv = new Pair<>("7911", Room.LIVE_PLAT_HY);
    public static final Pair<String, Integer> pair_112 = new Pair<>("7911112", Room.LIVE_PLAT_HY);

    public static final String WEB_SOCKET_DY = "wss://danmuproxy.douyu.com:8506/";

    public static final String WEB_SOCKET_HY = "wss://cdnws.api.huya.com";

    public static List<Pair<String, Integer>> getSubscribeHost() {
        List<Pair<String, Integer>> hosts = new ArrayList<>();
        hosts.add(pair_mming);
        hosts.add(pair_zgg01);
        hosts.add(pair_running);
        hosts.add(pair_xdd);
        hosts.add(pair_qqq);
        hosts.add(pair_tgltn);
        hosts.add(pair_xxlu);
        hosts.add(pair_godv);
        hosts.add(pair_112);
        return hosts;
    }

}
