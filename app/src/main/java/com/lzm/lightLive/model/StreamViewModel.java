package com.lzm.lightLive.model;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import com.lzm.lightLive.http.bean.dy.DyStream;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.request.dy.DyStreamHttpRequest;
import com.lzm.lightLive.util.CommonUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StreamViewModel extends ViewModel {

    private static final String TAG = "StreamViewModel";

    DyStreamHttpRequest mDyStreamCall = RetrofitManager.getDyStreamRetrofit().create(DyStreamHttpRequest.class);

    public interface OnStreamInfoResultListener {
        void onResult(String high, String low);
        void onError(Throwable throwable);
    }

    public void requestRoomStreamInfo(String id, OnStreamInfoResultListener resultListener) {
        String time = String.valueOf(System.currentTimeMillis());
        String sign = CommonUtil.encrypt2ToMD5(id + time);
        mDyStreamCall.queryRoomStreamInfo(id, time, sign, id, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<DyStream>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResult<DyStream> stringBaseResult) {
                        DyStream data = stringBaseResult.getData();
                        String live = data.getRtmpLive().split("_")[0];
                        String high = "http://hw-tct.douyucdn.cn/live/" + live + ".flv?uuid=";
                        String low = "http://hw-tct.douyucdn.cn/live/" + live + "_900.flv?uuid=";
                        resultListener.onResult(high, low);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage() );
                        resultListener.onError(e);
                    }

                    @Override
                    public void onComplete() { }
                });
    }

}
