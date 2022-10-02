package com.lzm.lightLive.video.service

import android.os.Binder
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.lzm.lib_base.BaseBindingOverlayService
import com.lzm.lib_base.view.DispatchKeyEventListener
import com.lzm.lib_base.view.SessionConstraintLayout
import com.lzm.lightLive.App
import com.lzm.lightLive.R
import com.lzm.lightLive.act.IntroActivity
import com.lzm.lightLive.databinding.LayoutFloatVideoBinding
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.model.RoomViewModel
import com.lzm.lightLive.util.AnimUtil

class FloatingService : BaseBindingOverlayService<FloatingService.PlayerBinder, LayoutFloatVideoBinding?,
        RoomViewModel>(), View.OnClickListener{

    companion object {
        private const val TAG = "floatingService"

        private lateinit var mBinder: PlayerBinder
    }

    private var player: ExoPlayer? = null

    private var mDispatchKeyEventListener = DispatchKeyEventListener {
        if (it.keyCode == KeyEvent.KEYCODE_BACK) {
            if (App.appStateWatcher.callbacks.topActivity is IntroActivity) {
                Log.e(TAG, "back event" )
                stopPlay()
            }
            true
        }else
            false
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_float_video
    }

    override fun onCreate() {
        super.onCreate()

        mBinder = PlayerBinder()
        if (null == player) {
            player = ExoPlayer.Builder(this).build()
        }
        mBind!!.video.player = player
        iniView()
    }

    private fun iniView() {
        mBind?.root?.visibility = View.GONE
        mBind!!.hostBar.ivDan.visibility = View.GONE
//        mBind?.root?.dispatchKeyEventListener = mDispatchKeyEventListener
        mBind?.close?.setOnClickListener { stopPlay() }
    }

    override fun canNotDrawOverlays() {}

    private fun glanceHostBar() {
        AnimUtil.glance(mBind?.hostBar!!.llUi, 500)
    }

    override fun getBinder(): PlayerBinder {
        return mBinder
    }

    inner class PlayerBinder: Binder() {

        fun startPlay(roomInfo: Room?) {
            mBind?.root?.visibility = View.VISIBLE
            Log.e(TAG, "startPlay: ${roomInfo?.liveStreamUriHigh} $player" )
            if (TextUtils.isEmpty(roomInfo?.liveStreamUriHigh)) return
            roomInfo?.liveStreamUriHigh?.let { MediaItem.fromUri(it) }
                ?.let { player?.setMediaItem(it) }
            player?.prepare()
            player?.play()
        }

        fun stop() {
            player?.stop()
            player?.release()
            windowManager.removeView(mBind?.root)
            stopSelf(startId)
        }

        fun idle() {
            mBind?.root?.visibility = View.GONE
        }
    }

    fun stopPlay() {
        player?.stop()
        mBind?.root?.visibility = View.GONE
    }

    override fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(App.appStateWatcher.callbacks.topActivity)
    }

    override fun onClick(v: View?) {
        Log.e(TAG, "onClick: ${v?.id}" )
        when (v?.id) {
            R.id.close -> stopPlay()

        }
    }

    override fun getViewModel(): RoomViewModel {
        return RoomViewModel()
    }

    override fun setViewModel(viewModel: RoomViewModel?) {
        mBind?.viewModel = viewModel
    }

}