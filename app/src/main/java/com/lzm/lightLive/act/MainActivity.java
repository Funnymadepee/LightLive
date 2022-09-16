package com.lzm.lightLive.act;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.lzm.lib_base.BaseBindingActivity;
import com.lzm.lib_base.NotifyDialog;
import com.lzm.lightLive.R;
import com.lzm.lightLive.adapter.BulletScreenAdapter;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.databinding.ActivityMainBinding;
import com.lzm.lightLive.http.request.dy.DyConnect;
import com.lzm.lightLive.model.RoomViewModel;
import com.lzm.lightLive.util.CommonUtil;
import com.lzm.lightLive.video.VideoListener;

import org.java_websocket.client.WebSocketClient;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding, RoomViewModel> {

    private static final String TAG = "MainActivity";

    private Room roomInfo;

    private WebSocketClient connect;
    private ExoPlayer player;

    boolean mExoPlayerFullscreen;
    Dialog mFullScreenDialog;
    int mLastWidth;
    int mLastHeight;
    int lastOrientation;

    private BulletScreenAdapter adapter;
    private final List<DyConnect.DouYuDanMu> danmuList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected RoomViewModel getViewModel() {
        return new ViewModelProvider(MainActivity.this).get(RoomViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        roomInfo = bundle.getParcelable("room_info");
        Log.e(TAG, "bundleRoomInfo: " + roomInfo.toString() );
        if(null == roomInfo)
            return;
//        startTransition();
        initViewModel();
        initWindow();
        initView();
        initWebSocket();
        initFullScreenDialog();

        StyledPlayerView playerView = mBind.videoView;
        player = new ExoPlayer.Builder(this).build();

        player.addListener(new VideoListener());

        playerView.setPlayer(player);

        Log.e(TAG, "onCreate: " + roomInfo.getLiveStreamUri() );
        setStream(roomInfo.getLiveStreamUri());
        playerView.setFullscreenButtonClickListener(isFullScreen -> {
            System.err.println("全屏： " + isFullScreen);
        });
        mBind.videoView.setControllerVisibilityListener((StyledPlayerView.ControllerVisibilityListener) visibility -> {
            mBind.llTitleBar.setVisibility(visibility);
        });

        mBind.videoView.setFullscreenButtonClickListener(isFullScreen -> {
            Log.e(TAG, "onCreate: " + isFullScreen );
            if (isFullScreen) {
                openFullScreenDialog();
            }
        });
        mBind.tvDan.setOnClickListener(v -> {
            mBind.ryDm.setVisibility(mBind.ryDm.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
    }

    private void initViewModel() {
        mBind.setViewModel(mViewModel);
        mViewModel.mRoom.set(roomInfo);
        mViewModel.roomId.set(roomInfo.getRoomId());
        mViewModel.avatar.set(roomInfo.getHostAvatar());
        mViewModel.roomName.set(roomInfo.getRoomName());
        mViewModel.roomStatus.set(roomInfo.getStreamStatus() == Room.LIVE_STATUS_ON);
    }

    private void startTransition() {
        mBind.videoView.setTransitionName(getIntent().getStringExtra("CUS_TRANSITION_NAME_1"));
        mBind.ivAvatar.setTransitionName(getIntent().getStringExtra("CUS_TRANSITION_NAME_2"));
        // 3. 设置具体的动画
        MaterialContainerTransform mEnterTf = new MaterialContainerTransform();
        mEnterTf.setDuration(300L);
        mEnterTf.addTarget(mBind.videoView);
//        mEnterTf.addTarget(mBind.ivAvatar);

        MaterialContainerTransform mExitTf = new MaterialContainerTransform();
        mExitTf.setDuration(300L);
        mExitTf.addTarget(mBind.videoView);
//        mExitTf.addTarget(mBind.ivAvatar);

        getWindow().setSharedElementEnterTransition(mEnterTf);
        getWindow().setSharedElementExitTransition(mExitTf);
    }

    private void setStream(String uri) {
        if(TextUtils.isEmpty(uri))
            return;
        if(player.isPlaying()) {
            player.stop();
        }
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        if(!CommonUtil.currentIsWifiConnected(this)) {
            NotifyDialog dialog = new NotifyDialog(this, R.layout.layout_dialog_info, Gravity.CENTER);
            dialog.setAutoDismiss(false);
            dialog.setTouchMovable(false);
            dialog.setScrollDismissAble(false);
            dialog.setCancelable(false);
            TextView btnNo = dialog.findById(R.id.btn_no);
            TextView btnYes = dialog.findById(R.id.btn_yes);
            TextView title = dialog.findById(R.id.tv_title);
            TextView content = dialog.findById(R.id.tv_content);
            title.setText("流量不要钱？");
            content.setText("继续使用流量观看？？？");
            btnYes.setOnClickListener(v->{
                player.play();
            });
            btnNo.setOnClickListener(v->{
                dialog.dismiss();
            });
            dialog.show();
        }else {
            player.play();
        }
    }

    private void initWebSocket() {
        DyConnect dyConnect = new DyConnect(roomInfo, new DyConnect.MessageReceiveListener() {
            @Override
            public void onReceiver(DyConnect.DouYuDanMu douYuDanMu) {
                danmuList.add(douYuDanMu);
                runOnUiThread(() -> {
                    adapter.setDataList(danmuList);
                    mBind.ryDm.smoothScrollToPosition(adapter.getItemCount() - 1);
                });
            }

            @Override
            public void onConnectError(Exception e) {

            }

            @Override
            public void onConnectClose(int code, String reason, boolean remote) {

            }
        });
        try {
            connect = dyConnect.createConnect();
            connect.connectBlocking();
        }catch (Exception e) {
            Log.e(TAG, "initWebSocket: " + e.getMessage() );
        }
    }

    private void initWindow() {
        mBind.llTitleBar.setOnApplyWindowInsetsListener((v, insets) -> {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
            lp.topMargin = insets.getSystemWindowInsetTop();
            return insets;
        });
    }

    private void initView() {
        adapter = new BulletScreenAdapter(danmuList);
        mBind.ryDm.setLayoutManager(new LinearLayoutManager(this));
        mBind.ryDm.setAdapter(adapter);
    }

    private void initFullScreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            private long mBackPressed;
            @Override
            public void onBackPressed() {
                if (mExoPlayerFullscreen
                        && mBackPressed <= System.currentTimeMillis()) {
                    mBackPressed = 1000 + System.currentTimeMillis();
                    Toast.makeText(MainActivity.this, "再次按下返回键退出", Toast.LENGTH_SHORT).show();
                    return;
                }
                closeFullScreenDialog();
                super.onBackPressed();
            }
        };
    }
    private void openFullScreenDialog() {
        mLastHeight = mBind.videoView.getHeight();
        mLastWidth = mBind.videoView.getWidth();
        lastOrientation = getRequestedOrientation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mFullScreenDialog.show();
        ((ViewGroup) mBind.videoView.getParent()).removeView(mBind.videoView);
        mFullScreenDialog.setContentView(mBind.videoView,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        mBind.videoView.setPadding(0,0,0,0);
        mBind.videoView.setFocusable(false);
        mExoPlayerFullscreen = true;
    }

    private void closeFullScreenDialog() {
        ((ViewGroup) mBind.videoView.getParent()).removeView(mBind.videoView);
        mBind.llContent.addView(mBind.videoView,0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mFullScreenDialog.dismiss();
        setRequestedOrientation(lastOrientation);
        mExoPlayerFullscreen = false;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        if (getActionBar() != null) {
            getActionBar().show();
        }
    }

    @Override
    public void onBackPressed() {
        connect.close();
        player.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}