package com.lzm.lightLive.http.dy;

import com.lzm.lightLive.bean.douyu.DyStream;
import com.lzm.lightLive.http.BaseResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DyStreamHttpRequest {

    @POST("lapi/live/hlsH5Preview/{roomId}?did=10000000000000000000000000001501")
    Observable<BaseResult<DyStream>> queryRoomStreamInfo(
            @Header("rid") String rid,
            @Header("time") String time,
            @Header("auth") String auth,
            @Path("roomId") String roomId,
            @Query("rid") String room_id
    );
}
