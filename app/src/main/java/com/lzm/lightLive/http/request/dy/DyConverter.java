package com.lzm.lightLive.http.request.dy;

import android.util.Log;

import com.lzm.lightLive.http.CustomGsonConvertFactory;

import java.io.IOException;

import okhttp3.ResponseBody;

public class DyConverter extends CustomGsonConvertFactory.ResponseConverter {

    @Override
    protected String convertResponse(ResponseBody value) throws IOException {
        String response = value.string();
        Log.e("TAG", response );
        return response;
    }
}
