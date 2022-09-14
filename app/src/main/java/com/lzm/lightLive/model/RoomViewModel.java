package com.lzm.lightLive.model;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lzm.lightLive.bean.douyu.DyRoom;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.dy.DyHttpRequest;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RoomViewModel extends ViewModel {

    private static final String TAG = "RoomViewModel";
    public final ObservableField<DyRoom> room = new ObservableField<>();
    public final ObservableField<String> roomName = new ObservableField<>();
    public final ObservableField<String> roomId = new ObservableField<>();
    public final ObservableField<String> avatar = new ObservableField<>();
    public final ObservableBoolean roomStatus = new ObservableBoolean(false);

    DyHttpRequest mDyCall = RetrofitManager.getDYRetrofit().create(DyHttpRequest.class);

    public MutableLiveData<DyRoom> requestRoomInfo(String id) {
        final MutableLiveData<DyRoom> roomMutableLiveData = new MutableLiveData<>();
        mDyCall.queryRoomInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<DyRoom>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(BaseResult<DyRoom> dyRoomBaseResult) {
                        room.set(dyRoomBaseResult.getData());
                        roomName.set(dyRoomBaseResult.getData().getRoomName());
                        roomId.set(dyRoomBaseResult.getData().getRoomId());
                        avatar.set(dyRoomBaseResult.getData().getAvatar());
                        roomStatus.set(dyRoomBaseResult.getData().getRoomStatus() == 1);
                        roomMutableLiveData.setValue(dyRoomBaseResult.getData());
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
        return roomMutableLiveData;
    }
}
