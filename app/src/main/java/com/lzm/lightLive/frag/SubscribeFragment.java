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
import com.lzm.lightLive.common.Const;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.bean.dy.DyRoom;
import com.lzm.lightLive.http.bean.hy.HyRoom;
import com.lzm.lightLive.http.request.dy.DyHttpRequest;
import com.lzm.lightLive.http.request.hy.HyHttpRequest;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubscribeFragment extends BaseFreshFragment<Room> {

    private static final String TAG = "SubscribeFragment";

    private HostAdapter mAdapter;

    private int showStatus = -1;

    public SubscribeFragment() {
    }

    public SubscribeFragment(int showStatus) {
        this.showStatus = showStatus;
    }

    List<Room> roomList;

    @Override
    protected void getData() {
        if(pageNum == 0) {
            requestSubHostInfo(Const.getSubscribeHost());
        }else
            setRefresh(false);
    }

    @Override
    public void onRefresh() {
//        super.onRefresh();
        //todo grab form database
        requestSubHostInfo(Const.getSubscribeHost());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        roomList = new ArrayList<>();
        mAdapter = new HostAdapter(R.layout.layout_item_host, roomList);
        setAdapter(mAdapter);
        onRefresh();
        mAdapter.setEnableLoadMore(false);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context, 2);
    }

    private void requestSubHostInfo(List<Room> roomPair) {
        mAdapter.getData().clear();
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
                                        if(result.getData().getStreamStatus() == showStatus) {
                                            mAdapter.addData(0, result.getData());
                                        }
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
                                        if(result.getData().getStreamStatus() == showStatus) {
                                            mAdapter.addData(0, result.getData());
                                        }
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
}