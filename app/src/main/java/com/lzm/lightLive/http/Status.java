package com.lzm.lightLive.http;


/** Http状态*/
//todo 确认是否使用枚举？
public class Status {

    public static int LOADING = 800;

    /*Response 响应后结果*/
    public static int SUCCESS = 801;
    public static int FAILURE = 802;

    public static int ERROR = 803;

    public static String OK = "OK";
    public static String MSG_ERROR = "Unknown Error";
}
