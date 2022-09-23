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
import com.lzm.lightLive.adapter.CateAdapter;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.bean.dy.DyCate;
import com.lzm.lightLive.http.bean.dy.DySortRoom;
import com.lzm.lightLive.http.request.dy.DyHttpRequest;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryFragment extends BaseFreshFragment<DySortRoom> {

    private static final String TAG = "CategoryFragment";

    private CateAdapter mAdapter;

    private int platform = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            this.platform = bundle.getInt("platform");
        }
    }

    @Override
    protected void getData() {
        if(pageNum == 0) {
            requestSubHostInfo(platform);
        }else
            setRefresh(false);
    }

    @Override
    public void onRefresh() {
        requestSubHostInfo(platform);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new CateAdapter(R.layout.layout_item_cate);
        setAdapter(mAdapter);
        onRefresh();
        mAdapter.setEnableLoadMore(false);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context, 3);
    }

    private void requestSubHostInfo(int platform) {
        mAdapter.getData().clear();
        DyHttpRequest dyHttpRequest = RetrofitManager.getDyRetrofit().create(DyHttpRequest.class);
        if (platform == Room.LIVE_PLAT_DY) {
            dyHttpRequest.queryAllCategories()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DyCate>() {
                        @Override
                        public void onSubscribe(Disposable d) { }

                        @Override
                        public void onNext(DyCate result) {
                            if(mAdapter != null
                                    && null != result.getData()) {
                                mAdapter.addData(result.getData());
                                setRefresh(false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: " + e.getMessage() );
                        }

                        @Override
                        public void onComplete() { }
                    });
        }
    }
}