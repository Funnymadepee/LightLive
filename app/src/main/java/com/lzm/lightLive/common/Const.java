package com.lzm.lightLive.common;

import com.lzm.lightLive.http.bean.Room;
import java.util.ArrayList;
import java.util.List;

public class Const {


    public static final Room pair_pdd = new Room("101", Room.LIVE_PLAT_DY);
    public static final Room pair_dsm = new Room("606118", Room.LIVE_PLAT_DY);
    public static final Room pair_dy_pubg = new Room("100", Room.LIVE_PLAT_DY);
    public static final Room pair_xdd = new Room("3168536", Room.LIVE_PLAT_DY);

    public static final Room pair_godv = new Room("7911", Room.LIVE_PLAT_HY);
    public static final Room pair_pcl = new Room("660004", Room.LIVE_PLAT_HY);


    public static final String WEB_SOCKET_DY = "wss://danmuproxy.douyu.com:8506/";

    public static final String WEB_SOCKET_HY = "wss://cdnws.api.huya.com";

    public static String AVATAR_URL_DY = "https://apic.douyucdn.cn/upload/";

    public static List<Room> getSubscribeHost() {
        List<Room> hosts = new ArrayList<>();

        hosts.add(pair_dy_pubg);
        hosts.add(pair_xdd);
        hosts.add(pair_pdd);
        hosts.add(pair_dsm);

        hosts.add(pair_godv);
        hosts.add(pair_pcl);
        return hosts;
    }

}
