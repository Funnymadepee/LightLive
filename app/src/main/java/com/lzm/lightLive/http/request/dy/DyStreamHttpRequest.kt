package com.lzm.lightLive.http.request.dy

import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.bean.dy.DyStream
import io.reactivex.Observable
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DyStreamHttpRequest {
    @POST("lapi/live/hlsH5Preview/{roomId}?did=10000000000000000000000000001501")
    fun queryRoomStreamInfo(
        @Header("rid") rid: String?,
        @Header("time") time: String?,
        @Header("auth") auth: String?,
        @Path("roomId") roomId: String?,
        @Query("rid") room_id: String?
    ): Observable<BaseResult<DyStream?>>
}