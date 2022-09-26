package com.lzm.lightLive.http

/*请求返回数据 基础封装*/
class BaseResult<T> {
    var errorCode = 0
    var errorMsg: String? = null
    var data: T? = null
        private set

    constructor(data: T) {
        this.data = data
    }

    constructor(code: Int, msg: String?) {
        errorCode = code
        errorMsg = msg
    }

    fun setData(data: T) {
        this.data = data
    }

    val isSuccess: Boolean
        get() = RESULT_SUCCESS == errorCode

    override fun toString(): String {
        return "BaseResult{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}'
    }

    companion object {
        const val RESULT_SUCCESS = 0
    }
}