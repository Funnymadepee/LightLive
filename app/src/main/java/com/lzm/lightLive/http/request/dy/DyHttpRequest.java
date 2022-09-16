package com.lzm.lightLive.http.request.dy;

import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.bean.dy.DyRoom;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DyHttpRequest{
    /*获取房间信息*/
    @GET("api/RoomApi/room/{roomId}")
    Observable<BaseResult<DyRoom>> queryRoomInfo(@Path("roomId")String roomId);

}
