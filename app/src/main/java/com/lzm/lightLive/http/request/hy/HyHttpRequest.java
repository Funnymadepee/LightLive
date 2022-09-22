package com.lzm.lightLive.http.request.hy;

import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.bean.hy.HySortRoom;
import com.lzm.lightLive.http.bean.hy.HyRoom;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HyHttpRequest{

    @GET("cache.php?m=Live&do=profileRoom")
    Observable<BaseResult<HyRoom>> queryRoomInfo(@Query("roomid") String roomId);

    @GET("cache.php?m=LiveList&do=getLiveListByPage&tagAll=0&callback=getLiveListJsonpCallback")
    Observable<HySortRoom> queryHeatRoom(@Query("page") int page);

    @GET("{room}")
    @Headers("user-agent: Mozilla/5.0 " +
            "(Linux; Android 6.0; Nexus 5 Build/MRA58N) " +
            "AppleWebKit/537.36 " +
            "(KHTML,like Gecko) " +
            "Chrome/79.0.3945.88 Mobile Safari/537.36")
    Observable<Response<ResponseBody>> queryRoomInfoHtml(@Path("room") String room);
}
