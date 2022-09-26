package com.lzm.lightLive.http

import com.lzm.lightLive.http.CustomGsonConvertFactory.ResponseConverter
import com.lzm.lightLive.http.request.hy.HyBasicConverter
import com.lzm.lightLive.http.request.hy.HyRecommendConverter
import com.lzm.lightLive.http.request.hy.RequestInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private const val BASE_URL_DY = "https://www.douyu.com/"
    private const val BASE_URL_DY_OPEN = "https://open.douyucdn.cn/"
    private const val BASE_URL_DY_STREAM = "https://playweb.douyucdn.cn/"
    private const val BASE_URL_HY = "https://www.huya.com/"
    private const val BASE_URL_HY_Mobile = "https://m.huya.com/"
    private const val BASE_URL_HY_MP = "https://mp.huya.com/"
    private const val BASE_URL_BL = "https://www.wanandroid.com/"

    val dyRetrofit: Retrofit
        get() = RetrofitInstance.dyRetrofit
    val dyOpenRetrofit: Retrofit
        get() = RetrofitInstance.dyOpenRetrofit
    val dyStreamRetrofit: Retrofit
        get() = RetrofitInstance.dyStreamRetrofit
    val hyMpRetrofit: Retrofit
        get() = RetrofitInstance.hyMpRetrofit
    val hyRetrofit: Retrofit
        get() = RetrofitInstance.hyRetrofit
    val hyMobileRetrofit: Retrofit
        get() = RetrofitInstance.hyMobileRetrofit

    private fun buildRetrofit(url: String): Retrofit {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun buildRetrofit(
        url: String, converter: ResponseConverter
    ): Retrofit {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(CustomGsonConvertFactory.create(converter))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    private object RetrofitInstance {
        val dyRetrofit = buildRetrofit(BASE_URL_DY)
        val dyOpenRetrofit = buildRetrofit(BASE_URL_DY_OPEN)
        val dyStreamRetrofit = buildRetrofit(BASE_URL_DY_STREAM)
        val hyRetrofit = buildRetrofit(BASE_URL_HY, HyRecommendConverter())
        val hyMobileRetrofit = buildRetrofit(BASE_URL_HY_Mobile)
        val hyMpRetrofit = buildRetrofit(BASE_URL_HY_MP, HyBasicConverter())
    }
}