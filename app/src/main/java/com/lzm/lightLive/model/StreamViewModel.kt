package com.lzm.lightLive.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.dy.DyStream
import com.lzm.lightLive.http.request.dy.DyStreamHttpRequest
import com.lzm.lightLive.util.CommonUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class StreamViewModel : ViewModel() {
    var mDyStreamCall: DyStreamHttpRequest = RetrofitManager.dyStreamRetrofit.create(
        DyStreamHttpRequest::class.java
    )

    interface OnStreamInfoResultListener {
        fun onResult(high: String?, low: String?)
        fun onError(throwable: Throwable?)
    }

    fun requestRoomStreamInfo(id: String?, resultListener: OnStreamInfoResultListener) {
        val time = System.currentTimeMillis().toString()
        val sign = CommonUtil.encrypt2ToMD5(id + time)
        mDyStreamCall.queryRoomStreamInfo(id, time, sign, id, id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResult<DyStream?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(stringBaseResult: BaseResult<DyStream?>) {
                    val data = stringBaseResult.data
                    val live = data?.rtmpLive?.split("_")?.toTypedArray()?.get(0)
                    val high = "http://hw-tct.douyucdn.cn/live/$live.flv?uuid="
                    val low = "http://hw-tct.douyucdn.cn/live/" + live + "_900.flv?uuid="
                    resultListener.onResult(high, low)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: " + e.message)
                    resultListener.onError(e)
                }

                override fun onComplete() {}
            })
    }

    companion object {
        private const val TAG = "StreamViewModel"
    }
}