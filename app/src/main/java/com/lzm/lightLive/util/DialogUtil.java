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
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.widget.AppCompatTextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.lzm.lightLive.R;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.bean.dy.DyCate;
import com.lzm.lightLive.view.RoundImageView;

public class DialogUtil {

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

                liveUri.setText(roomInfo.getLiveStreamUri());

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
                WebView webView = v.findViewById(R.id.webView);
                WebSettings webSettings= webView.getSettings();
                String userAgentString = webSettings.getUserAgentString();
                AppCompatTextView title = v.findViewById(R.id.tv_title);
                title.setText(cateData.getCateName());
                String url = "https://www.douyu.com/" + cateData.getCateUrl();
                webView.loadUrl(url);
                Log.e(url + "TAG", "onBind: " + userAgentString );
            }
        });
    }
}
