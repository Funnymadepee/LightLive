package com.lzm.lightLive.util;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.lzm.lightLive.R;
import com.lzm.lightLive.adapter.HostAdapter;
import com.lzm.lightLive.common.Const;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.bean.dy.DyCate;
import com.lzm.lightLive.http.bean.dy.DySortRoom;
import com.lzm.lightLive.http.request.dy.DyHttpRequest;
import com.lzm.lightLive.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DialogUtil {

    private static final String TAG = "DialogUtil";

    public static void showHostInfoDialog(Room roomInfo) {

        FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_dialog_host) {
            @Override
            public void onBind(FullScreenDialog dialog, View v) {
                RoundImageView avatar = v.findViewById(R.id.iv_avatar);
                AppCompatTextView hostName = v.findViewById(R.id.tv_host_name);
                AppCompatTextView roomName = v.findViewById(R.id.tv_room_name);
                AppCompatTextView roomId = v.findViewById(R.id.tv_room_id);
                AppCompatTextView cateName = v.findViewById(R.id.tv_cate_name);
                AppCompatTextView roomUrl = v.findViewById(R.id.tv_room_url);
                AppCompatTextView liveUri = v.findViewById(R.id.tv_live_uri);
                LottieAnimationView animView = v.findViewById(R.id.animView);

                Glide.with(v)
                        .load(roomInfo.getHostAvatar())
                        .into(avatar);
                hostName.setText(roomInfo.getHostName());
                roomName.setText(roomInfo.getRoomName());
                roomId.setText(roomInfo.getRoomId());
                cateName.setText(roomInfo.getCateName());
                String url = "";
                switch (roomInfo.getPlatform()) {
                    case Room.LIVE_PLAT_DY:
                        url = "https://www.douyu.com/" + roomInfo.getRoomId();
                        break;
                    case Room.LIVE_PLAT_HY:
                        url = "https://www.huya.com/" + roomInfo.getRoomId();
                        break;
                    case Room.LIVE_PLAT_BL:
                        url = "https://live.bilibili.com/" + roomInfo.getRoomId();
                        break;
                }
                SpannableString string = new SpannableString(url);
                string.setSpan(new UnderlineSpan(), 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                string.setSpan(new ForegroundColorSpan(Color.parseColor("#1144aa")),
                        0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                roomUrl.setText(string);

                roomUrl.setOnClickListener(tv->{
                    if(!TextUtils.isEmpty(roomUrl.getText())) {
                        Uri uri = Uri.parse(roomUrl.getText().toString());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        roomUrl.getContext().startActivity(intent);
                    }
                });

                liveUri.setText(roomInfo.getLiveStreamUriHigh());

                animView.setAnimation(R.raw.blue_bg);
                animView.setSpeed(2f);
                animView.playAnimation();
            }
        });
    }

    public static void showCateInfoDialog(DyCate.DyCateData cateData) {

        FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_dialog_cate) {
            @Override
            public void onBind(FullScreenDialog dialog, View v) {
                TextView tvCateName = v.findViewById(R.id.tv_cate_name);
                RecyclerView recyclerView = v.findViewById(R.id.ry_cate);
                recyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
                tvCateName.setText(cateData.getCateName());

                List<Room> roomList = new ArrayList<>();
                HostAdapter mAdapter = new HostAdapter(R.layout.layout_item_host, roomList);
                mAdapter.setPageMaxItem(50);
                recyclerView.setAdapter(mAdapter);

                DyHttpRequest call = RetrofitManager.getDyRetrofit().create(DyHttpRequest.class);

                call.queryCateHosts(cateData.getCateId(), 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BaseResult<DySortRoom>>() {
                            @Override
                            public void onSubscribe(Disposable d) { }

                            @Override
                            public void onNext(BaseResult<DySortRoom> result) {
                                Log.e(TAG, "onNext: " + result.getData() );
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
                                        _temp.setHostAvatar(Const.AVATAR_URL_DY + room.getAvatar() + prefix);
                                        _temp.setThumbUrl(room.getThumb());
                                        roomList.add(_temp);
                                    }
                                    mAdapter.setMassiveData(roomList);
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
        });
    }

}
