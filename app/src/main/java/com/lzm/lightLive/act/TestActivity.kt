package com.lzm.lightLive.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lzm.lightLive.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

//        HyHttpRequest mCall = RetrofitManager.getHyMpRetrofit().create(HyHttpRequest.class);
//        HyDanMuConnect danMuConnect = new HyDanMuConnect();
//        danMuConnect.getHtml();
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

    companion object {
        private const val TAG = "Test"
    }
}