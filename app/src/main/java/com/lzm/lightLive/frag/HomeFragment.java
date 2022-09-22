package com.lzm.lightLive.frag;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lzm.lib_base.BaseFreshFragment;
import com.lzm.lightLive.R;
import com.lzm.lightLive.adapter.HostAdapter;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.bean.dy.DyRoom;
import com.lzm.lightLive.http.bean.dy.DySortRoom;
import com.lzm.lightLive.http.bean.hy.HyRoom;
import com.lzm.lightLive.http.request.dy.DyHttpRequest;
import com.lzm.lightLive.http.request.hy.HyHttpRequest;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends BaseFreshFragment<Room> {

    private static final String TAG = "HomeFragment";

    private HostAdapter mAdapter;

    List<Room> roomList = new ArrayList<>();

    @Override
    protected void getData() {
        if(pageNum > 0) {
            requestHotHost();
        }else {
            mAdapter.getCacheList().clear();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        requestHotHost();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        roomList = Const.getSubscribeHost();
        mAdapter = new HostAdapter(R.layout.layout_item_host, roomList);
        setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context, 2);
    }

    static String dy_base = "https://apic.douyucdn.cn/upload/";

    DyHttpRequest call2 = RetrofitManager.getDyRetrofit().create(DyHttpRequest.class);

    private void requestHotHost() {
        call2.queryLiveHeatSort(pageNum + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<DySortRoom>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseResult<DySortRoom> result) {
                        if(null != result.getData()) {
                            List<Room> roomList = new ArrayList<>();
                            for (DySortRoom.Related room : result.getData().getRelatedList()) {
                                Room _temp = new Room(String.valueOf(room.getRoomId()), Room.LIVE_PLAT_DY);
                                _temp.setRoomId(String.valueOf(room.getRoomId()));
                                _temp.setCateName(room.getCateName());
                                _temp.setHeatNum(room.getOnline());
                                _temp.setStreamStatus(Room.LIVE_STATUS_ON);
                                _temp.setRoomName(room.getRoomName());
                                _temp.setHostName(room.getNickName());
                                String prefix = ".jpg";
                                if(room.getAvatar().contains("avatar")
                                        || room.getAvatar().contains("avanew")) {
                                    prefix = "_big.jpg";

                                }
                                _temp.setHostAvatar(dy_base + room.getAvatar() + prefix);
                                _temp.setThumbUrl(room.getThumb());
                                roomList.add(_temp);
                            }
                            Log.e(TAG, "onNext: " + pageNum  + " roomList: " + roomList.size());
                            mAdapter.setMassiveData(roomList);
                            setRefresh(false);
                            baseAdapter.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: " + pageNum );
                    }
                });
    }

    private void requestSubHostInfo(List<Room> roomPair) {
        for (Room pair : roomPair) {
            switch (pair.getPlatform()) {
                case Room.LIVE_PLAT_DY:
                    DyHttpRequest mDyCall = RetrofitManager.getDyOpenRetrofit().create(DyHttpRequest.class);
                    mDyCall.queryRoomInfo(pair.getRoomId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<BaseResult<DyRoom>>() {
                                @Override
                                public void onSubscribe(Disposable d) {}

                                @Override
                                public void onNext(BaseResult<DyRoom> result) {
                                    if(mAdapter != null
                                            && null != result.getData()) {
                                        mAdapter.addData(result.getData());
                                        setRefresh(false);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    baseAdapter.loadMoreFail();
                                    Log.e(TAG, "mDyCall onError: " + e.getMessage() );
                                }

                                @Override
                                public void onComplete() {}
                            });
                    break;
                case Room.LIVE_PLAT_HY:
                    HyHttpRequest mHyCall = RetrofitManager.getHyMpRetrofit().create(HyHttpRequest.class);
                    mHyCall.queryRoomInfo(pair.getRoomId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<BaseResult<HyRoom>>() {
                                @Override
                                public void onSubscribe(Disposable d) {}

                                @Override
                                public void onNext(BaseResult<HyRoom> result) {
                                    if(mAdapter != null
                                            && null != result.getData()) {
                                        mAdapter.addData(result.getData());
                                        setRefresh(false);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    baseAdapter.loadMoreFail();
                                    Log.e(TAG, "mHyCall onError: " + e.getMessage() );
                                }

                                @Override
                                public void onComplete() {}
                            });
                    break;
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        int size = mAdapter.getData().size();
        int cachedSize = mAdapter.getCacheList().size();

        Log.e(TAG, "onLoadMoreRequested: " + pageNum + " dataSize: " + size + " cachedSize: " + cachedSize);
        if(cachedSize > size) {
            Log.e(TAG, "onLoadMoreRequested: " + size + " max: " +  (size + HostAdapter.PAGE_MAX_ITEM));
            if(cachedSize > (size + HostAdapter.PAGE_MAX_ITEM)) {
                mAdapter.addData(size, mAdapter.getCacheList().subList(size, size + HostAdapter.PAGE_MAX_ITEM));
            }else {
                Log.e(TAG, "onLoadMoreRequested: " + cachedSize );
                mAdapter.addData(size, mAdapter.getCacheList().subList(size, cachedSize));
            }
            baseAdapter.loadMoreComplete();
        }else {
            ++pageNum;
            super.onLoadMoreRequested();
        }
    }
}