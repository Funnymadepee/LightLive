package com.lzm.lightLive.act;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lzm.lightLive.R;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.request.hy.HyHttpRequest;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        HyHttpRequest mCall = RetrofitManager.getHYRetrofit().create(HyHttpRequest.class);

       /* mCall.getRoomInfo("791102")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<HyRoom>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult<HyRoom> result) {
                        Log.e(TAG, "onNext: " + result.getData().toString() );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage() );
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

}