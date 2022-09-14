package com.lzm.lightLive.common;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Const {
    public static final String xdd = "http://hw-tct.douyucdn.cn/live/3168536r2fd7Av5z.flv?uuid=";
    public static final String qqq = "http://hw-tct.douyucdn.cn/live/19223r7Gez8YkcIT.flv?uuid=";
    public static final String running = "http://hw-tct.douyucdn.cn/live/2448877rnNapRkkq.flv?uuid=";
    public static final String zgg01 = "http://hw-tct.douyucdn.cn/live/296059rsdAOF21lW.flv?uuid=";
    public static final String mming = "http://hw-tct.douyucdn.cn/live/6822146r8MsAgAfr.flv?uuid=";
    public static final String godv = "https://ws.flv.huya.com/src/518969542-518969542-2228957210510098432-1038062540-10057-A-0-1-imgplus.flv?wsSecret=1b78f6d531220356212df6894a6a4a24&wsTime=63208e80&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mp&txyp=o%3Aq5%3B&fs=bgct&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=102";
    public static final String xxlu = "https://ws.flv.huya.com/src/1578701695-1578701695-6780472150164766720-3157526846-10057-A-0-1.flv?wsSecret=1dfbde2d75524c6b7bc6afe2579966d4&wsTime=631b5ba3&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mp&txyp=o%3Aczeic1%3B&fs=bgct&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&exsphd=264_500,264_2000,264_4000,&t=102";
    public static final String tgltn = "https://ws.flv.huya.com/src/1199614251094-1199614251094-5662379699297320960-2399228625644-10057-A-0-1.flv?wsSecret=063312faabe02f19d3c5eb930f3f9ed3&wsTime=6318c792&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mp&fs=bgct&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&exsphd=264_500,264_2000,264_4000,&t=102";

    public static final Pair<String, String> pair_xdd = new Pair<>(xdd, "3168536");
    public static final Pair<String, String> pair_mming = new Pair<>(mming, "6822146");
    public static final Pair<String, String> pair_zgg01 = new Pair<>(zgg01, "296059");
    public static final Pair<String, String> pair_qqq = new Pair<>(qqq, "19223");
    public static final Pair<String, String> pair_running = new Pair<>(running, "2448877");
    public static final Pair<String, String> pair_tgltn = new Pair<>(tgltn, "1199614251094");
    public static final Pair<String, String> pair_xxlu = new Pair<>(xxlu, "791133");
    public static final Pair<String, String> pair_godv = new Pair<>(godv, "7911");

    public static final String WEB_SOCKET_DY = "wss://danmuproxy.douyu.com:8506/";

    public static List<Pair<String, String>> getSubscribeHost() {
        List<Pair<String, String>> hosts = new ArrayList<>();
        hosts.add(pair_mming);
        hosts.add(pair_zgg01);
        hosts.add(pair_running);
        hosts.add(pair_xdd);
        hosts.add(pair_qqq);
        return hosts;
    }

    public static List<String> getSubscribeHostString() {
        List<String> hosts = new ArrayList<>();
        hosts.add("3168536");
        hosts.add("6822146");
        hosts.add("296059");
        hosts.add("19223");
        hosts.add("2448877");
        return hosts;
    }

}
