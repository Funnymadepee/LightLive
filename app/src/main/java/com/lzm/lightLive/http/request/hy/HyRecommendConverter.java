package com.lzm.lightLive.http.request.hy;

import com.lzm.lightLive.http.CustomGsonConvertFactory;
import java.io.IOException;
import okhttp3.ResponseBody;

public class HyRecommendConverter extends CustomGsonConvertFactory.ResponseConverter {

    @Override
    public String convertResponse(ResponseBody value) throws IOException {
        String valueStr = value.string();
        return valueStr.substring("getLiveListJsonpCallback(".length(), valueStr.length() - 1);
    }

}
