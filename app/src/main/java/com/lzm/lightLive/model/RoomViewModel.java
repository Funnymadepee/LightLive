package com.lzm.lightLive.model;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.bean.dy.DyRoom;
import com.lzm.lightLive.http.bean.hy.HyRoom;
import com.lzm.lightLive.http.request.dy.DyHttpRequest;
import com.lzm.lightLive.http.request.hy.HyHttpRequest;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RoomViewModel extends ViewModel {

    private static final String TAG = "RoomViewModel";

    public final ObservableField<Room> mRoom = new ObservableField<>();
    public final ObservableField<String> roomName = new ObservableField<>();
    public final ObservableField<String> roomId = new ObservableField<>();
    public final ObservableField<String> avatar = new ObservableField<>();
    public final ObservableBoolean roomStatus = new ObservableBoolean(false);

    DyHttpRequest mDyCall = RetrofitManager.getDyOpenRetrofit().create(DyHttpRequest.class);

    HyHttpRequest mHyCall = RetrofitManager.getHyMpRetrofit().create(HyHttpRequest.class);

    public MutableLiveData<Room> requestRoomInfo(Room room) {
        final MutableLiveData<Room> roomMutableLiveData = new MutableLiveData<>();
        int platform = room.getPlatform();
        switch (platform) {
            case Room.LIVE_PLAT_DY:
                mDyCall.queryRoomInfo(room.getRoomId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BaseResult<DyRoom>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(BaseResult<DyRoom> result) {
                                onResponse(result.getData(), roomMutableLiveData);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                break;
            case Room.LIVE_PLAT_HY:
                mHyCall.queryRoomInfo(room.getRoomId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BaseResult<HyRoom>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(BaseResult<HyRoom> result) {
                                onResponse(result.getData(), roomMutableLiveData);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                break;
        }
        return roomMutableLiveData;
    }

    private void onResponse(Room room, MutableLiveData<Room> roomMutableLiveData) {
        mRoom.set(room);
        roomName.set(room.getRoomName());
        roomId.set(room.getRoomId());
        avatar.set(room.getHostAvatar());
        roomStatus.set(room.getStreamStatus() == 1);
        roomMutableLiveData.setValue(room);
    }
}
