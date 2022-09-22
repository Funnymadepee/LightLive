package com.lzm.lightLive.http;

/*请求返回数据 基础封装*/
public class BaseResult<T> {

    public static final int RESULT_SUCCESS = 0;

    private int errorCode;

    private String errorMsg;

    private T data;

    public BaseResult(T data) {
        this.data = data;
    }

    public BaseResult(int code, String msg) {
        this.errorCode = code;
        this.errorMsg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return RESULT_SUCCESS == errorCode;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }

}
