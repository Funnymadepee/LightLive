package com.lzm.lightLive.http.request.hy

import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.bean.hy.HyRoom
import com.lzm.lightLive.http.bean.hy.HySortRoom
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface HyHttpRequest {
    @GET("cache.php?m=Live&do=profileRoom")
    fun queryRoomInfo(@Query("roomid") roomId: String?): Observable<BaseResult<HyRoom?>>

    @GET("cache.php?m=LiveList&do=getLiveListByPage&tagAll=0&callback=getLiveListJsonpCallback")
    fun queryHeatRoom(@Query("page") page: Int): Observable<HySortRoom?>?

    @GET("{room}")
    @Headers(
        "user-agent: Mozilla/5.0 " +
                "(Linux; Android 6.0; Nexus 5 Build/MRA58N) " +
                "AppleWebKit/537.36 " +
                "(KHTML,like Gecko) " +
                "Chrome/79.0.3945.88 Mobile Safari/537.36"
    )
    fun queryRoomInfoHtml(@Path("room") room: String?): Observable<Response<ResponseBody>>
}