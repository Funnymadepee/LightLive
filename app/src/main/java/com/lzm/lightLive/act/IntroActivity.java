package com.lzm.lightLive.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
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
import com.lzm.lightLive.bean.douyu.DyRoom;
import com.lzm.lightLive.common.Const;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.dy.DyHttpRequest;
import com.lzm.lightLive.model.RoomViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        window.setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        RecyclerView room = findViewById(R.id.rv_room);
        List<Pair<String, String>> subscribeHost = Const.getSubscribeHost();

        HostAdapter adapter = new HostAdapter(this, subscribeHost);
        room.setLayoutManager(new LinearLayoutManager(this));
        room.setAdapter(adapter);
    }
}