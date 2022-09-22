package com.lzm.lightLive.http;

import com.lzm.lightLive.http.request.dy.DyConverter;
import com.lzm.lightLive.http.request.hy.HyBasicConverter;
import com.lzm.lightLive.http.request.hy.HyRecommendConverter;
import com.lzm.lightLive.http.request.hy.RequestInterceptor;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String BASE_URL_DY = "https://www.douyu.com/";
    private static final String BASE_URL_DY_OPEN = "https://open.douyucdn.cn/";
    private static final String BASE_URL_DY_STREAM = "https://playweb.douyucdn.cn/";

    private static final String BASE_URL_HY = "https://www.huya.com/";
    private static final String BASE_URL_HY_M = "https://m.huya.com/";
    private static final String BASE_URL_HY_MP = "https://mp.huya.com/";
    private static final String BASE_URL_BL = "https://www.wanandroid.com/";

    private RetrofitManager() { }

    public static Retrofit getDyRetrofit() {
        return RetrofitInstance.dyRetrofit;
    }

    public static Retrofit getDyOpenRetrofit() {
        return RetrofitInstance.dyOpenRetrofit;
    }

    public static Retrofit getDyStreamRetrofit() {
        return RetrofitInstance.dyStreamRetrofit;
    }

    public static Retrofit getHyMpRetrofit() {
        return RetrofitInstance.hyMpRetrofit;
    }

    public static Retrofit getHyRetrofit() {
        return RetrofitInstance.hyRetrofit;
    }

    public static Retrofit getHyMRetrofit() {
        return RetrofitInstance.hyMRetrofit;
    }

    private static class RetrofitInstance {
        private static final Retrofit dyRetrofit = buildRetrofit(BASE_URL_DY, new DyConverter());
        private static final Retrofit dyOpenRetrofit = buildRetrofit(BASE_URL_DY_OPEN);
        private static final Retrofit dyStreamRetrofit = buildRetrofit(BASE_URL_DY_STREAM);

        private static final Retrofit hyRetrofit = buildRetrofit(BASE_URL_HY, new HyRecommendConverter());
        private static final Retrofit hyMRetrofit = buildRetrofit(BASE_URL_HY_M);
        private static final Retrofit hyMpRetrofit = buildRetrofit(BASE_URL_HY_MP, new HyBasicConverter());
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

    private static Retrofit buildRetrofit(String url
            , CustomGsonConvertFactory.ResponseConverter converter) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(CustomGsonConvertFactory.create(converter))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    private static Retrofit buildHyMRetrofit(String url, CustomGsonConvertFactory.ResponseConverter converter) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(CustomGsonConvertFactory.create(converter))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
