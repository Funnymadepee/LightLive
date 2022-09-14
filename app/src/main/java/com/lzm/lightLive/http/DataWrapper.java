package com.lzm.lightLive.http;


import androidx.annotation.Nullable;

import com.bumptech.glide.load.HttpException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;

public class DataWrapper<T> {

    public int status;

    @Nullable
    public T data;

    @Nullable
    public String message;

    public Throwable error;

    public DataWrapper(int status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public DataWrapper(int status, Throwable t) {
        this.status = status;
        this.error = t;
    }

    public static <T> DataWrapper<T> loading() {
        return new DataWrapper<>(Status.LOADING, null, null);
    }

    public static <T> DataWrapper<T> loading(@Nullable T data){
        return new DataWrapper<>(Status.LOADING, data, null);
    }

    public static <T> DataWrapper<T> response(@Nullable BaseResult<T> data){
        if (data!=null){
            if(data.isSuccess()){
                return new DataWrapper<>(Status.SUCCESS,data.getData(),Status.OK);
            }else
                return new DataWrapper<>(Status.FAILURE,null,data.getErrorMsg());
        }
        return new DataWrapper<>(Status.ERROR,null,Status.MSG_ERROR);
    }

    public static <T> DataWrapper<T> error(Throwable t) {
        showErrorMessage(t);
        return new DataWrapper<>(Status.ERROR, t);
    }

    private static void showErrorMessage(Throwable t){
        if (t instanceof HttpException) {
//            ToastUtil.showToast("网路错误");
        } else if (t instanceof JsonParseException || t instanceof JSONException) {
//            ToastUtil.showToast("解析错误");
        } else if (t instanceof ConnectException) {
//            ToastUtil.showToast("连接错误");
        } else {
//            ToastUtil.showToast("错误：" + t.getMessage());
        }
    }
}
