package com.lzm.lightLive.act

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView.ControllerVisibilityListener
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.lzm.lib_base.BaseBindingActivity
import com.lzm.lib_danmu.DanMuView
import com.lzm.lightLive.R
import com.lzm.lightLive.adapter.DanMuAdapter
import com.lzm.lightLive.databinding.ActivityMainBinding
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.danmu.basic.*
import com.lzm.lightLive.http.danmu.dy.DyDanMuConnector
import com.lzm.lightLive.http.danmu.hy.HyDanMuConnector
import com.lzm.lightLive.model.RoomViewModel
import com.lzm.lightLive.model.StreamViewModel
import com.lzm.lightLive.model.StreamViewModel.OnStreamInfoResultListener
import com.lzm.lightLive.util.CacheUtil
import com.lzm.lightLive.util.CommonUtil
import com.lzm.lightLive.util.DialogUtil
import com.lzm.lightLive.util.UiTools
import com.lzm.lightLive.video.service.FloatingService
import com.lzm.lightLive.view.BasicDanMuModel

class MainActivity : BaseBindingActivity<ActivityMainBinding?, RoomViewModel?>(), Player.Listener {
    private var roomInfo: Room? = null
    private var mDanMuConnector: DanMuServiceConnector? = null
    private var player: ExoPlayer? = null
    var mExoPlayerFullscreen = false
    var mFullScreenDialog: Dialog? = null
    private var mLastWidth = 0
    private var mLastHeight = 0
    private var fullScreenDanMuView: DanMuView? = null
    private var mDanMuAdapter: DanMuAdapter? = null
    private var mediaItemHigh: MediaItem? = null
    private var mediaItemLow: MediaItem? = null
    private var streamViewModel: StreamViewModel? = null
    private var lastConfiguration: Configuration? = null
    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): RoomViewModel {
        return ViewModelProvider(this@MainActivity)[RoomViewModel::class.java]
    }

    override fun setViewModel(viewModel: RoomViewModel?) {
        mBind!!.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val window = window
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        lastConfiguration = resources.configuration //保存初始模式
        statusBar.setStatusBarBackground(R.drawable.shape_trans_down)
        val bundle = intent.getBundleExtra("bundle")
        roomInfo = bundle!!.getParcelable("room_info")
        if (null == roomInfo) return
        //        startTransition();
        initViewModel()
        initWindow()
        initView()
        initViewClickListener()
        initFullScreenDialog()
        if (roomInfo!!.liveStreamUriHigh.isNullOrEmpty().not()) {
            mediaItemHigh = roomInfo?.liveStreamUriHigh?.let { MediaItem.fromUri(it) }
        } else {
            streamViewModel!!.requestRoomStreamInfo(
                roomInfo!!.roomId,
                object : OnStreamInfoResultListener {
                    override fun onResult(high: String?, low: String?) {
                        roomInfo?.liveStreamUriLow = low
                        roomInfo?.liveStreamUriHigh = high
                        mediaItemLow = MediaItem.fromUri(high!!)
                    }

                    override fun onError(throwable: Throwable?) {
                        TipDialog.show(R.string.no_stream_hint, WaitDialog.TYPE.ERROR)
                    }
                })
        }
        if (!TextUtils.isEmpty(roomInfo?.liveStreamUriLow)) {
            mediaItemLow = roomInfo?.liveStreamUriLow?.let { MediaItem.fromUri(it) }
        }
        setStream(mediaItemHigh)
    }

    private fun initViewClickListener() {
        mBind!!.hostBar.ivAvatar.setOnLongClickListener {
            DialogUtil.showHostInfoDialog(roomInfo)
            false
        }
        mBind!!.ivBack.setOnClickListener { finish() }
        mBind!!.videoView.setControllerVisibilityListener(ControllerVisibilityListener { visibility: Int ->
            mBind!!.llTitleBar.visibility = visibility
        })
        mBind!!.videoView.setFullscreenButtonClickListener { isFullScreen: Boolean ->
            if (isFullScreen) {
                openFullScreenDialog()
            }
        }
        mBind!!.hostBar.ivDan.setOnClickListener {
            //现在是否正在显示弹幕,则接下来要关闭弹幕显示
            val isNowShowingDanMu = mDanMuAdapter!!.isDanMuUpdate
            mBind!!.hostBar.ivDan.setImageResource(if (isNowShowingDanMu) R.drawable.ic_danmu_off else R.drawable.ic_danmu_on)
            mDanMuAdapter?.isDanMuUpdate = !isNowShowingDanMu
            UiTools.performHapticClick(mBind!!.hostBar.ivDan)
        }
        mBind!!.hostBar.ivDefinition.setOnClickListener { v: View ->
            if (null != player && null != mediaItemHigh && null != mediaItemLow) {
                mBind!!.hostBar.ivDefinition.setImageResource(if (mediaItemHigh == player!!.currentMediaItem) R.drawable.ic_definition_low else R.drawable.ic_definition_hd)
                startPlay(if (mediaItemHigh == player!!.currentMediaItem) mediaItemLow else mediaItemHigh)
            } else {
                UiTools.snackShort(v, R.string.no_other_definition)
            }
        }
        mBind!!.loading.setOnClickListener { startPlay(if (mediaItemHigh == player!!.currentMediaItem) mediaItemHigh else mediaItemLow) }
    }

    private fun initViewModel() {
        streamViewModel = StreamViewModel()
        mBind!!.viewModel = mViewModel
        mViewModel!!.mRoom.set(roomInfo)
        mViewModel!!.roomId.set(roomInfo?.roomId)
        mViewModel!!.avatar.set(roomInfo?.hostAvatar)
        mViewModel!!.roomName.set(roomInfo?.roomName)
        mViewModel!!.roomStatus.set(roomInfo?.streamStatus == Room.LIVE_STATUS_ON)
    }

    private fun startTransition() {
        mBind!!.videoView.transitionName = intent.getStringExtra("CUS_TRANSITION_NAME_1")
        mBind!!.hostBar.ivAvatar.transitionName = intent.getStringExtra("CUS_TRANSITION_NAME_2")
        // 3. 设置具体的动画
        val mEnterTf = MaterialContainerTransform()
        mEnterTf.duration = 300L
        mEnterTf.addTarget(mBind!!.videoView)
        //        mEnterTf.addTarget(mBind.ivAvatar);
        val mExitTf = MaterialContainerTransform()
        mExitTf.duration = 300L
        mExitTf.addTarget(mBind!!.videoView)
        //        mExitTf.addTarget(mBind.ivAvatar);
        window.sharedElementEnterTransition = mEnterTf
        window.sharedElementExitTransition = mExitTf
    }

    private fun setStream(mediaItem: MediaItem?) {
        if (!CommonUtil.currentIsWifiConnected(this)
            && !CacheUtil.getNoPromptCellDataDialog(this)
        ) {
            MessageDialog.show(
                R.string.using_cell_data,
                R.string.continue_to_watch,
                R.string.confirm,
                R.string.cancel,
                R.string.no_prompt
            )
                .setOkButton { _: MessageDialog?, _: View? ->
                    initWebSocket()
                    startPlay(mediaItem)
                    false
                }.setCancelButton { _: MessageDialog?, _: View? ->
                    finish()
                    false
                }.setOtherButton { _: MessageDialog?, _: View? ->
                    CacheUtil.setNoPromptCellDataDialog(this, true)
                    false
                }
        } else {
            initWebSocket()
            startPlay(mediaItem)
        }
    }

    private fun startPlay(mediaItem: MediaItem?) {
        if (null == mediaItem) {
            TipDialog.show(R.string.no_stream_hint, WaitDialog.TYPE.WARNING)
            return
        }
        if (null == player) {
            player = ExoPlayer.Builder(this).build()
            player!!.addListener(this)
            mBind!!.videoView.player = player
        }
        player!!.setMediaItem(mediaItem)
        player!!.prepare()
        player!!.play()
    }

    private fun stopPlay() {
        player!!.stop()
        player!!.release()
        player = null
    }

    private fun initWebSocket() {
        val socketListener: WebSocketListener = object : WebSocketListener() {
            override fun onMessage(danMu: DanMu) {
                super.onMessage(danMu)
                runOnUiThread {
                    if (mDanMuAdapter!!.isDanMuUpdate) {
                        val danMuModel = BasicDanMuModel(this@MainActivity, danMu.content)
                        danMuModel.textColor = danMu.danMuFormatData!!.fontColor
                        if (mExoPlayerFullscreen && null != fullScreenDanMuView) {
                            fullScreenDanMuView!!.add(danMuModel)
                        }
                        mDanMuAdapter!!.addData(danMu)
                    }
                }
            }
        }
        when (roomInfo?.platform) {
            Room.LIVE_PLAT_DY -> mDanMuConnector =
                DyDanMuConnector(this, roomInfo, socketListener)
            Room.LIVE_PLAT_HY -> mDanMuConnector =
                HyDanMuConnector(this, roomInfo, socketListener)
        }
    }

    private fun initWindow() {
        mBind!!.llTitleBar.setOnApplyWindowInsetsListener { v: View, insets: WindowInsets ->
            val lp = v.layoutParams as FrameLayout.LayoutParams
            lp.topMargin = insets.systemWindowInsetTop
            insets
        }
    }

    private fun initView() {
        mDanMuAdapter =
            DanMuAdapter(mBind!!.ryDm, R.layout.layout_item_dm, mBind!!.viewToBottom.root)
        mBind!!.ryDm.layoutManager = LinearLayoutManager(this)
        mBind!!.ryDm.addOnScrollListener(mDanMuAdapter!!.scrollListener)
    }

    private fun initFullScreenDialog() {
        mFullScreenDialog =
            object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    closeFullScreenDialog()
                    super.onBackPressed()
                }
            }
    }

    private fun openFullScreenDialog() {
        mLastHeight = mBind!!.videoView.height
        mLastWidth = mBind!!.videoView.width
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mFullScreenDialog!!.show()
        mExoPlayerFullscreen = true
        (mBind!!.videoView.parent as ViewGroup).removeView(mBind!!.videoView)
        val fullScreenFrameLayout = FrameLayout(mFullScreenDialog!!.context)
        val fullScreenLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mFullScreenDialog!!.setContentView(fullScreenFrameLayout, fullScreenLayoutParams)
        fullScreenDanMuView = DanMuView(mFullScreenDialog!!.context, null)
        fullScreenDanMuView!!.isInterceptTouchEvent = false
        fullScreenDanMuView!!.prepare()
        fullScreenFrameLayout.removeAllViews()
        fullScreenFrameLayout.addView(mBind!!.videoView, fullScreenLayoutParams)
        fullScreenFrameLayout.addView(fullScreenDanMuView, fullScreenLayoutParams)
        mBind!!.videoView.setPadding(0, 0, 0, 0)
        mBind!!.videoView.isFocusable = false
    }

    private fun closeFullScreenDialog() {
        (mBind!!.videoView.parent as ViewGroup).removeView(mBind!!.videoView)
        fullScreenDanMuView = null
        mBind!!.llContent.addView(
            mBind!!.videoView, 0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        mFullScreenDialog!!.dismiss()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        mExoPlayerFullscreen = false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Configuration.UI_MODE_NIGHT_NO   浅色模式
        // Configuration.UI_MODE_NIGHT_YES  深色模式
        val currentMediaItem = player!!.currentMediaItem
        val lastUiMode = lastConfiguration!!.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK != lastUiMode) {
            stopPlay()
            startPlay(currentMediaItem)
        }
        if (newConfig.orientation != lastConfiguration!!.orientation
            && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        ) {
            openFullScreenDialog()
        }
        lastConfiguration = newConfig
    }

    override fun onDestroy() {
        mDanMuConnector!!.close()
        if (CacheUtil.getFloatWhenExit(this)
            && null != player!!.currentMediaItem
        ) {
            FloatingService.startFloatingService(this, roomInfo)
        }
        stopPlay()
        super.onDestroy()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {

                //播放器没有可播放的媒体。
                System.err.println("播放器没有可播放的媒体")
            }
            Player.STATE_BUFFERING -> {

                //播放器无法立即从当前位置开始播放。这种状态通常需要加载更多数据时发生。
                System.err.println("正在加载数据")
                mBind!!.loading.visibility = View.VISIBLE
                mBind!!.loading.postDelayed({ mBind!!.retry.setText(R.string.click_retry) }, 5000)
            }
            Player.STATE_READY -> {

                // 播放器可以立即从当前位置开始播放。如果{@link#getPlayWhenReady（）}为true，否则暂停。
                //当点击暂停或者播放时都会调用此方法
                //当跳转进度时，进度加载完成后调用此方法
                System.err.println("播放器可以立即从当前位置开始")
                mBind!!.retry.setText(R.string.blank)
                mBind!!.loading.visibility = View.GONE
            }
            Player.STATE_ENDED -> {

                //播放器完成了播放
                System.err.println("视频结束了噢")
                mBind!!.retry.setText(R.string.blank)
                mBind!!.loading.visibility = View.VISIBLE
            }
        }
    }

    companion object {
//        private const val TAG = "MainActivity"
    }

}