package com.lzm.lightLive.http.request.hy;

import android.util.Log;
import com.lzm.lightLive.http.CustomGsonConvertFactory;
import java.io.IOException;
import okhttp3.ResponseBody;

public class HyConverter extends CustomGsonConvertFactory.ResponseConverter {

    @Override
    public String convertResponse(ResponseBody value) throws IOException {
        String valueStr = value.string();
        Log.e("convertResponse: ", valueStr );
        return valueStr;
    }

}
