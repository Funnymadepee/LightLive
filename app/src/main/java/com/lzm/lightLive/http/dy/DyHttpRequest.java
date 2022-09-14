package com.lzm.lightLive.http.dy;

import com.lzm.lightLive.bean.douyu.DyRoom;
import com.lzm.lightLive.http.BaseResult;

import java.util.Date;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DyHttpRequest {

    /*获取房间信息*/
    @GET("api/RoomApi/room/{roomId}")
    Observable<BaseResult<DyRoom>> queryRoomInfo(@Path("roomId")String roomId);

}
