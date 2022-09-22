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
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.lzm.lib_base.BaseBindingActivity;
import com.lzm.lib_base.NotifyDialog;
import com.lzm.lightLive.R;
import com.lzm.lightLive.adapter.DanMuAdapter;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.databinding.ActivityMainBinding;
import com.lzm.lightLive.http.request.dy.DyDanMuConnect;
import com.lzm.lightLive.model.RoomViewModel;
import com.lzm.lightLive.util.CommonUtil;
import com.lzm.lightLive.util.DialogUtil;
import com.lzm.lightLive.util.UiTools;
import com.lzm.lightLive.video.VideoListener;
import com.lzm.lightLive.view.BasicDanMuModel;

import org.java_websocket.client.WebSocketClient;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding, RoomViewModel> implements Player.Listener {

    private static final String TAG = "MainActivity";

    private Room roomInfo;

    private WebSocketClient danMuWebSocketClient;
    private ExoPlayer player;

    boolean mExoPlayerFullscreen;
    Dialog mFullScreenDialog;
    int mLastWidth;
    int mLastHeight;
    int lastOrientation;

    private DanMuAdapter mDanMuAdapter;

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
        UiTools.setStatusBar(this, false);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        roomInfo = bundle.getParcelable("room_info");
        if(null == roomInfo)
            return;
//        startTransition();
        initViewModel();
        initWindow();
        initView();
        initDanmu();
        initViewClickListener();
        initWebSocket();
        initFullScreenDialog();

        StyledPlayerView playerView = mBind.videoView;
        player = new ExoPlayer.Builder(this).build();

        player.addListener(new VideoListener());

        playerView.setPlayer(player);

        setStream(roomInfo.getLiveStreamUri());
        playerView.setFullscreenButtonClickListener(isFullScreen -> System.err.println("全屏： " + isFullScreen));
        mBind.videoView.setControllerVisibilityListener((StyledPlayerView.ControllerVisibilityListener) visibility -> {
            mBind.llTitleBar.setVisibility(visibility);
        });

        mBind.videoView.setFullscreenButtonClickListener(isFullScreen -> {
            if (isFullScreen) {
                openFullScreenDialog();
            }
        });
        mBind.tvDan.setOnClickListener(v -> {
            mBind.ryDm.setVisibility(mBind.ryDm.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
    }

    private void initViewClickListener() {
        mBind.ivAvatar.setOnLongClickListener(v -> {
            DialogUtil.showHostInfoDialog(roomInfo);
            return false;
        });
    }

    private void initDanmu() {
        mBind.danmu.prepare();
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
            btnNo.setOnClickListener(v-> dialog.dismiss());
            dialog.show();
        }else {
            player.play();
        }
    }

    private void initWebSocket() {
        DyDanMuConnect dyConnect = new DyDanMuConnect(roomInfo, new DyDanMuConnect.MessageReceiveListener() {
            @Override
            public void onReceiver(DyDanMuConnect.DouYuDanMu douYuDanMu) {
                if(null != douYuDanMu) {
                    BasicDanMuModel danMuView = new BasicDanMuModel(MainActivity.this, douYuDanMu.getTxt());
                    mBind.danmu.add(danMuView);
                    runOnUiThread(() -> {
                        mDanMuAdapter.addData(douYuDanMu);
                    });
                }
            }
        });
        try {
            danMuWebSocketClient = dyConnect.createConnect();
            danMuWebSocketClient.connectBlocking();
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
        mDanMuAdapter = new DanMuAdapter(mBind.ryDm, R.layout.layout_item_dm, mBind.viewToBottom.getRoot());
        mBind.ryDm.setLayoutManager(new LinearLayoutManager(this));
        mBind.ryDm.addOnScrollListener(mDanMuAdapter.getScrollListener());

    }

    private void initFullScreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
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
        danMuWebSocketClient.close();
        player.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_IDLE: {
                //播放器没有可播放的媒体。
                System.err.println("播放器没有可播放的媒体");
                break;
            }
            case Player.STATE_BUFFERING: {
                //播放器无法立即从当前位置开始播放。这种状态通常需要加载更多数据时发生。
                System.err.println("正在加载数据");
                mBind.spinKit.setVisibility(View.VISIBLE);
                break;
            }
            case Player.STATE_READY: {
                // 播放器可以立即从当前位置开始播放。如果{@link#getPlayWhenReady（）}为true，否则暂停。
                //当点击暂停或者播放时都会调用此方法
                //当跳转进度时，进度加载完成后调用此方法
                System.err.println("播放器可以立即从当前位置开始");
                mBind.spinKit.setVisibility(View.GONE);
                break;
            }
            case Player.STATE_ENDED: {
                //播放器完成了播放
                System.err.println("视频结束了噢");
                mBind.spinKit.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

}