package com.lzm.lightLive.model

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.bean.dy.DyRoom
import com.lzm.lightLive.http.bean.hy.HyRoom
import com.lzm.lightLive.http.request.dy.DyHttpRequest
import com.lzm.lightLive.http.request.hy.HyHttpRequest
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RoomViewModel : ViewModel() {
    val mRoom = ObservableField<Room?>()
    val roomName = ObservableField<String?>()
    val roomId = ObservableField<String?>()
    val avatar = ObservableField<String?>()
    val roomStatus = ObservableBoolean(false)
    var mDyCall: DyHttpRequest = RetrofitManager.dyOpenRetrofit.create(DyHttpRequest::class.java)
    var mHyCall: HyHttpRequest = RetrofitManager.hyMpRetrofit.create(HyHttpRequest::class.java)

    fun requestRoomInfo(room: Room): MutableLiveData<Room?> {
        val roomMutableLiveData = MutableLiveData<Room?>()
        when (room.platform) {
            Room.LIVE_PLAT_DY -> mDyCall.queryRoomInfo(room.roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<DyRoom?>> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(result: BaseResult<DyRoom?>) {
                        onResponse(result.data, roomMutableLiveData)
                    }

                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                })
            Room.LIVE_PLAT_HY -> mHyCall.queryRoomInfo(room.roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<HyRoom?>> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(result: BaseResult<HyRoom?>) {
                        onResponse(result.data, roomMutableLiveData)
                    }

                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                })
        }
        return roomMutableLiveData
    }

    private fun onResponse(room: Room?, roomMutableLiveData: MutableLiveData<Room?>) {
        mRoom.set(room)
        roomName.set(room?.roomName)
        roomId.set(room?.roomId)
        avatar.set(room?.hostAvatar)
        roomStatus.set(room?.streamStatus == Room.LIVE_STATUS_ON)
        roomMutableLiveData.value = room
    }

    companion object {
        private const val TAG = "RoomViewModel"
    }
}