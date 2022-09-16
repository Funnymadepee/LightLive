package com.lzm.lightLive.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Window;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
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
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IntroActivity extends AppCompatActivity {

    private HostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        window.setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        requestSubHostInfo(Const.getSubscribeHost());
    }

    private void initView() {
        RecyclerView room = findViewById(R.id.rv_room);
        adapter = new HostAdapter(this);
        room.setLayoutManager(new GridLayoutManager(this, 2));
        room.setAdapter(adapter);
    }

    private void requestSubHostInfo(List<Pair<String, Integer>> roomPair) {
        initView();
        for (Pair<String, Integer> pair : roomPair) {
            switch (pair.second) {
                case Room.LIVE_PLAT_DY:
                    DyHttpRequest mDyCall = RetrofitManager.getDYRetrofit().create(DyHttpRequest.class);
                    mDyCall.queryRoomInfo(pair.first)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<BaseResult<DyRoom>>() {
                                @Override
                                public void onSubscribe(Disposable d) {}

                                @Override
                                public void onNext(BaseResult<DyRoom> result) {
                                    adapter.addData(result.getData());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("TAG", "onError: " + e.getMessage() );
                                }

                                @Override
                                public void onComplete() {}
                            });
                    break;
                case Room.LIVE_PLAT_HY:
                    HyHttpRequest mHyCall = RetrofitManager.getHYRetrofit().create(HyHttpRequest.class);
                    mHyCall.queryRoomInfo(pair.first)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<BaseResult<HyRoom>>() {
                                @Override
                                public void onSubscribe(Disposable d) {}

                                @Override
                                public void onNext(BaseResult<HyRoom> result) {
                                    adapter.addData(result.getData());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("TAG", "onError: " + e.getMessage() );
                                }

                                @Override
                                public void onComplete() {}
                            });
                    break;
            }
        }
    }

}