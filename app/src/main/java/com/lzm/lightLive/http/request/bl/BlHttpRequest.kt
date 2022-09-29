package com.lzm.lightLive.http.request.bl

import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.bean.bl.BLRoom
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BlHttpRequest {

    @GET("room/v1/Room/room_init")
    fun getRoomInfo(@Query("id") roomId: String?): Observable<BaseResult<BLRoom?>>

    @GET("room/v1/Room/get_status_info_by_uids")
    fun getUidInfo(@Query("uids[]") uid: Array<Long?>): Observable<BaseResult<BLRoom?>>

    @GET("room/v1/Room/playUrl?otype=json")
    fun getStreamUrl(@Query("cid") cid: Long?,
                     @Query("platform") platform: String?,  //h5, web
                     @Query("quality") quality: Int? ): Observable<BaseResult<BLRoom?>>
}