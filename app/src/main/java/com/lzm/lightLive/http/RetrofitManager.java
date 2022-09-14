package com.lzm.lightLive.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final String BASE_URL_DY = "https://open.douyucdn.cn/";
    private static final String BASE_URL_DY_STREAM = "https://playweb.douyucdn.cn/";
    private static final String BASE_URL_HY = "https://www.wanandroid.com/";
    private static final String BASE_URL_BL = "https://www.wanandroid.com/";

    private RetrofitManager() {

    }

    public static Retrofit getDYRetrofit() {
        return RetrofitInstance.dyRetrofit;
    }

    public static Retrofit getDYStreamRetrofit() {
        return RetrofitInstance.dyStreamRetrofit;
    }

    public static Retrofit getHYRetrofit() {
        return RetrofitInstance.hyRetrofit;
    }

    private static Retrofit buildRetrofit(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static class RetrofitInstance {
        private static Retrofit dyRetrofit = buildRetrofit(BASE_URL_DY);
        private static Retrofit dyStreamRetrofit = buildRetrofit(BASE_URL_DY_STREAM);
        private static Retrofit hyRetrofit = buildRetrofit(BASE_URL_HY);
    }
}
