package com.lzm.lightLive.video.service

import android.app.Activity
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.lzm.lib_base.BaseBindingOverlayService
import com.lzm.lib_base.util.DoubleClickListener
import com.lzm.lightLive.R
import com.lzm.lightLive.databinding.LayoutFloatVideoBinding
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.util.AnimUtil

class FloatingService : BaseBindingOverlayService<FloatingService.PlayerBinder, LayoutFloatVideoBinding?>() {
    private var player: ExoPlayer? = null
    override fun getLayoutId(): Int {
        return R.layout.layout_float_video
    }

    override fun iniView(bundle: Bundle) {
        initClick()
        val room = bundle.getParcelable<Room>(KEY) ?: return
        if (TextUtils.isEmpty(room.liveStreamUriHigh)) return
        if (null == player) {
            player = ExoPlayer.Builder(this).build()
            mBind!!.floatVideoView.player = player
        }
        Glide.with(this)
            .load(room.hostAvatar)
            .into(mBind!!.hostBar.ivAvatar)
        mBind!!.hostBar.ivDan.visibility = View.GONE
        glanceHostBar()
        AnimUtil.translationYAnim(
            mBind!!.hostBar.root,
            500,
            mBind!!.hostBar.llUi.height.toFloat(),
            0f
        )
        room.liveStreamUriHigh?.let { MediaItem.fromUri(it) }?.let { player!!.setMediaItem(it) }
        player!!.prepare()
        player!!.play()
    }

    private fun initClick() {
        mBind?.root?.setOnClickListener(DoubleClickListener(5, 800) {
            Log.e(TAG, "initClick: " )
        })
        mBind?.close?.setOnClickListener {
            player?.stop()
            player?.release()
            windowManager.removeView(mBind?.root)
            stopSelf(startId)
        }
    }

    override fun canNotDrawOverlays() {}

    private fun glanceHostBar() {
        AnimUtil.glance(mBind?.hostBar!!.llUi, 500)
    }

    companion object {
        private const val KEY = "room_Info"
        private const val TAG = "floatingService"
        fun startFloatingService(activity: Activity?, roomInfo: Room?) {
            if (null != activity) {
                val bundle = Bundle()
                bundle.putParcelable(KEY, roomInfo)
                val intent = Intent(activity, FloatingService::class.java)
                intent.putExtras(bundle)
                activity.startService(intent)
            }
        }
    }

    override fun getBinder(): PlayerBinder {
        return PlayerBinder()
    }


    open class PlayerBinder: Binder() {
        //fun functionRemote() {}
    }

}