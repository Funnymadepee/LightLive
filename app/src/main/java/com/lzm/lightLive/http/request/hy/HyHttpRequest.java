package com.lzm.lightLive.http.request.hy;

import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.bean.hy.HyRoom;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HyHttpRequest{

    @GET("cache.php?m=Live&do=profileRoom")
    Observable<BaseResult<HyRoom>> queryRoomInfo(@Query("roomid") String roomId);

}
