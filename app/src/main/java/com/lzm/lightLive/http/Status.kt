package com.lzm.lightLive.http

/** Http状态 */ //todo 确认是否使用枚举？
object Status {
    var LOADING = 800

    /*Response 响应后结果*/
    var SUCCESS = 801
    var FAILURE = 802
    var ERROR = 803
    var OK = "OK"
    var MSG_ERROR = "Unknown Error"
}