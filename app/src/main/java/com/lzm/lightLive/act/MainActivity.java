package com.lzm.lightLive.act;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.lzm.lib_base.BaseBindingActivity;
import com.lzm.lib_danmu.DanMuView;
import com.lzm.lightLive.R;
import com.lzm.lightLive.adapter.DanMuAdapter;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.databinding.ActivityMainBinding;
import com.lzm.lightLive.http.danmu.basic.DanMu;
import com.lzm.lightLive.http.danmu.basic.DanMuServiceConnector;
import com.lzm.lightLive.http.danmu.dy.DyDanMuConnector;
import com.lzm.lightLive.http.danmu.basic.WebSocketListener;
import com.lzm.lightLive.http.danmu.hy.HyDanMuConnector;
import com.lzm.lightLive.model.RoomViewModel;
import com.lzm.lightLive.model.StreamViewModel;
import com.lzm.lightLive.util.CacheUtil;
import com.lzm.lightLive.util.CommonUtil;
import com.lzm.lightLive.util.DialogUtil;
import com.lzm.lightLive.util.UiTools;
import com.lzm.lightLive.view.BasicDanMuModel;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding, RoomViewModel>
        implements Player.Listener {

    private static final String TAG = "MainActivity";

    private Room roomInfo;

    private DanMuServiceConnector mDanMuConnector;

    private ExoPlayer player;

    boolean mExoPlayerFullscreen;
    Dialog mFullScreenDialog;
    int mLastWidth;
    int mLastHeight;
    int lastOrientation;
    private DanMuView fullScreenDanMuView;

    private DanMuAdapter mDanMuAdapter;

    private MediaItem mediaItemHigh;
    private MediaItem mediaItemLow;

    private StreamViewModel streamViewModel;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected RoomViewModel getViewModel() {
        return new ViewModelProvider(MainActivity.this).get(RoomViewModel.class);
    }

    @Override
    protected void setViewModel(RoomViewModel viewModel) {
        mBind.setViewModel(viewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        super.onCreate(savedInstanceState);
        statusBar.setStatusBarBackground(R.drawable.shape_trans_down);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        roomInfo = bundle.getParcelable("room_info");
        if(null == roomInfo)
            return;
//        startTransition();
        initViewModel();
        initWindow();
        initView();
        initViewClickListener();
        initFullScreenDialog();
        player = new ExoPlayer.Builder(this).build();
        player.addListener(this);
        mBind.videoView.setPlayer(player);

        if (!TextUtils.isEmpty(roomInfo.getLiveStreamUriHigh())) {
            mediaItemHigh = MediaItem.fromUri(roomInfo.getLiveStreamUriHigh());
        } else {
            streamViewModel.requestRoomStreamInfo(roomInfo.getRoomId(),
                    new StreamViewModel.OnStreamInfoResultListener() {
                        @Override
                        public void onResult(String high, String low) {
                            roomInfo.setLiveStreamUriLow(low);
                            roomInfo.setLiveStreamUriHigh(high);
                            mediaItemLow = MediaItem.fromUri(high);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            TipDialog.show(R.string.no_stream_hint, WaitDialog.TYPE.ERROR);
                        }
                    });
        }
        if (!TextUtils.isEmpty(roomInfo.getLiveStreamUriLow())) {
            mediaItemLow = MediaItem.fromUri(roomInfo.getLiveStreamUriLow());
        }
        setStream(mediaItemHigh);
    }

    private void initViewClickListener() {
        mBind.ivAvatar.setOnLongClickListener(v -> {
            DialogUtil.showHostInfoDialog(roomInfo);
            return false;
        });

        mBind.ivBack.setOnClickListener(v -> finish());

        mBind.videoView.setControllerVisibilityListener((StyledPlayerView.ControllerVisibilityListener) visibility -> {
            mBind.llTitleBar.setVisibility(visibility);
        });

        mBind.videoView.setFullscreenButtonClickListener(isFullScreen -> {
            if (isFullScreen) {
                openFullScreenDialog();
            }
        });

        mBind.ivDan.setOnClickListener(v -> {
            //现在是否正在显示弹幕,则接下来要关闭弹幕显示
            boolean isNowShowingDanMu = mDanMuAdapter.isDanMuUpdate();
            mBind.ivDan.setImageResource(isNowShowingDanMu ?
                    R.drawable.ic_danmu_off : R.drawable.ic_danmu_on);
            mDanMuAdapter.setDanMuUpdate(!isNowShowingDanMu);
            UiTools.performHapticClick(mBind.ivDan);
        });

        mBind.ivDefinition.setOnClickListener(v -> {
            if (null != player && null != mediaItemHigh && null != mediaItemLow) {
                mBind.ivDefinition.setImageResource(mediaItemHigh.equals(player.getCurrentMediaItem())
                        ? R.drawable.ic_definition_low : R.drawable.ic_definition_hd);
                startPlay(mediaItemHigh.equals(player.getCurrentMediaItem()) ? mediaItemLow : mediaItemHigh);
            }else {
                UiTools.snackShort(v, R.string.no_other_definition);
            }
        });

        mBind.loading.setOnClickListener(v -> {
            startPlay(mediaItemHigh.equals(player.getCurrentMediaItem()) ? mediaItemHigh : mediaItemLow);
        });
    }

    private void initViewModel() {
        streamViewModel = new StreamViewModel();
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

    private void setStream(MediaItem mediaItem) {
        if(player.isPlaying()) {
            player.stop();
        }
        if(!CommonUtil.currentIsWifiConnected(this)
                && !CacheUtil.getNoPromptCellDataDialog(this)) {
            MessageDialog.show(R.string.using_cell_data,
                            R.string.continue_to_watch,
                            R.string.confirm,
                            R.string.cancel,
                            R.string.no_prompt)
                    .setOkButton((dialog, v) -> {
                        initWebSocket();
                        startPlay(mediaItem);
                        return false;
                    }).setCancelButton((dialog, v) -> {
                        finish();
                        return false;
                    }).setOtherButton((dialog, v) -> {
                        CacheUtil.setNoPromptCellDataDialog(this, true);
                        return false;
                    });
        }else {
            initWebSocket();
            startPlay(mediaItem);
        }
    }

    private void startPlay(MediaItem mediaItem) {
        if (null == mediaItem) {
            TipDialog.show(R.string.no_stream_hint , WaitDialog.TYPE.WARNING);
            return;
        }
        if (player.isPlaying()) {
            player.stop();
        }
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    private void initWebSocket() {
        WebSocketListener socketListener = new WebSocketListener() {
            @Override
            public void onMessage(DanMu danMu) {
                super.onMessage(danMu);
                runOnUiThread(() -> {
                    if (mDanMuAdapter.isDanMuUpdate()) {
                        BasicDanMuModel danMuModel = new BasicDanMuModel(MainActivity.this, danMu.getContent());
                        danMuModel.textColor = danMu.getDanMuFormatData().getFontColor();
                        if (mExoPlayerFullscreen && null != fullScreenDanMuView) {
                            fullScreenDanMuView.add(danMuModel);
                        }
                        mDanMuAdapter.addData(danMu);
                    }
                });
            }
        };
        switch (roomInfo.getPlatform()) {
            case Room.LIVE_PLAT_DY:
                mDanMuConnector = new DyDanMuConnector(this, roomInfo, socketListener);
                break;
            case Room.LIVE_PLAT_HY:
                mDanMuConnector = new HyDanMuConnector(this, roomInfo,socketListener);
                break;
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
        mExoPlayerFullscreen = true;
        ((ViewGroup) mBind.videoView.getParent()).removeView(mBind.videoView);
        FrameLayout fullScreenFrameLayout = new FrameLayout(mFullScreenDialog.getContext());
        ViewGroup.LayoutParams fullScreenLayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mFullScreenDialog.setContentView(fullScreenFrameLayout, fullScreenLayoutParams);
        fullScreenDanMuView = new DanMuView(mFullScreenDialog.getContext(), null);
        fullScreenDanMuView.setInterceptTouchEvent(false);
        fullScreenDanMuView.prepare();
        fullScreenFrameLayout.removeAllViews();
        fullScreenFrameLayout.addView(mBind.videoView, fullScreenLayoutParams);
        fullScreenFrameLayout.addView(fullScreenDanMuView, fullScreenLayoutParams);
        mBind.videoView.setPadding(0,0,0,0);
        mBind.videoView.setFocusable(false);
    }

    private void closeFullScreenDialog() {
        ((ViewGroup) mBind.videoView.getParent()).removeView(mBind.videoView);
        fullScreenDanMuView = null;
        mBind.llContent.addView(mBind.videoView,0,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        mFullScreenDialog.dismiss();
        setRequestedOrientation(lastOrientation);
        mExoPlayerFullscreen = false;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            openFullScreenDialog();
        }else {
            closeFullScreenDialog();
        }
    }

    @Override
    public void finish() {
        mDanMuConnector.close();
        player.stop();
        super.finish();
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
                mBind.loading.setVisibility(View.VISIBLE);
                mBind.loading.postDelayed(()->{
                    mBind.retry.setText(R.string.click_retry);
                }, 5000);
                break;
            }
            case Player.STATE_READY: {
                // 播放器可以立即从当前位置开始播放。如果{@link#getPlayWhenReady（）}为true，否则暂停。
                //当点击暂停或者播放时都会调用此方法
                //当跳转进度时，进度加载完成后调用此方法
                System.err.println("播放器可以立即从当前位置开始");
                mBind.retry.setText(R.string.blank);
                mBind.loading.setVisibility(View.GONE);
                break;
            }
            case Player.STATE_ENDED: {
                //播放器完成了播放
                System.err.println("视频结束了噢");
                mBind.retry.setText(R.string.blank);
                mBind.loading.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

}