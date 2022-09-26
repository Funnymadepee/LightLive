package com.lzm.lightLive.http

import com.bumptech.glide.load.HttpException
import com.google.gson.JsonParseException
import org.json.JSONException
import java.net.ConnectException

class DataWrapper<T> {
    var status: Int
    var data: T? = null
    var message: String? = null
    var error: Throwable? = null

    constructor(status: Int, data: T?, message: String?) {
        this.status = status
        this.data = data
        this.message = message
    }

    constructor(status: Int, t: Throwable?) {
        this.status = status
        error = t
    }

    companion object {
        fun <T> loading(): DataWrapper<T?> {
            return DataWrapper(Status.LOADING, null, null)
        }

        fun <T> loading(data: T?): DataWrapper<T?> {
            return DataWrapper(Status.LOADING, data, null)
        }

        fun <T> response(data: BaseResult<T>?): DataWrapper<T?> {
            return if (data != null) {
                if (data.isSuccess) {
                    DataWrapper(Status.SUCCESS, data.data, Status.OK)
                } else DataWrapper(Status.FAILURE, null, data.errorMsg)
            } else DataWrapper(Status.ERROR, null, Status.MSG_ERROR)
        }

        fun <T> error(t: Throwable): DataWrapper<T> {
            showErrorMessage(t)
            return DataWrapper(Status.ERROR, t)
        }

        private fun showErrorMessage(t: Throwable) {
            when (t) {
                is HttpException -> {
                    System.err.println("HttpException: $t")
        //            ToastUtil.showToast("网路错误");
                }
                is JsonParseException, is JSONException -> {
                    System.err.println("JSON: ${t.message}")
        //            ToastUtil.showToast("解析错误");
                }
                is ConnectException -> {
                    System.err.println("connection: $t")
        //            ToastUtil.showToast("连接错误");
                }
                else -> {
                    System.err.println("other: $t")
        //            ToastUtil.showToast("错误：" + t.getMessage());
                }
            }
        }
    }
}