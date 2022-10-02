package com.lzm.lightLive.adapter

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.lzm.lib_base.NotifyDialog
import com.lzm.lib_base.adapter.BaseAdapter
import com.lzm.lightLive.App
import com.lzm.lightLive.R
import com.lzm.lightLive.act.IntroActivity
import com.lzm.lightLive.databinding.LayoutFloatVideoBinding
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.bean.dy.DyStream
import com.lzm.lightLive.http.request.dy.DyStreamHttpRequest
import com.lzm.lightLive.util.CommonUtil
import com.lzm.lightLive.util.DialogUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HostAdapter : BaseAdapter<Room> {

    companion object {
        private const val TAG = "HostAdapter"
    }

    constructor(layoutResId: Int, data: List<Room>?) : super(layoutResId, data)

    constructor(layoutResId: Int) : super(layoutResId)

    override fun convert(helper: BaseViewHolder, item: Room) {
        val context = helper.itemView.context
        Glide.with(context)
            .load(item.thumbUrl)
            .error(R.drawable.ic_live_no)
            .into(helper.getView<View>(R.id.iv_thumb) as ImageView)
        Glide.with(context)
            .load(item.hostAvatar)
            .error(R.drawable.ic_host)
            .into(helper.getView<View>(R.id.iv_avatar) as ImageView)
        helper.itemView.setOnClickListener { jumpToRoom(context, item) }
        helper.setImageDrawable(
            R.id.iv_plat,
            ActivityCompat.getDrawable(context, CommonUtil.generatePlatformDrawable(item.platform))
        )
        helper.setText(R.id.tv_live_cate, item.cateName)
            .setText(R.id.tv_host_name, item.hostName)
            .setText(R.id.tv_room_name, item.roomName)
            .setText(R.id.tv_room_num, item.roomId)
        if (item.heatNum > 0) {
            helper.getView<View>(R.id.tv_live_heat).visibility = View.VISIBLE
            helper.setText(R.id.tv_live_heat, CommonUtil.convertInt2K(item.heatNum))
        } else {
            helper.getView<View>(R.id.tv_live_heat).visibility = View.GONE
        }
        if (item.streamStatus == Room.LIVE_STATUS_ON) {
            helper.setText(R.id.tv_room_status, R.string.live_status_on)
            helper.getView<View>(R.id.tv_room_status).isActivated = true
        } else {
            helper.setText(R.id.tv_room_status, R.string.live_status_off)
            helper.getView<View>(R.id.tv_room_status).isActivated = false
        }
        helper.getView<View>(R.id.iv_avatar).setOnLongClickListener {
            DialogUtil.showHostInfoDialog(item)
            false
        }
    }

    private fun jumpToRoom(context: Context, room: Room) {
        if (room.platform == Room.LIVE_PLAT_DY) {
            WaitDialog.show(R.string.requesting)
            val time = System.currentTimeMillis().toString()
            val sign = CommonUtil.encrypt2ToMD5(room.roomId + time)
            val mDyStreamCall = RetrofitManager.dyStreamRetrofit.create(
                DyStreamHttpRequest::class.java
            )
            mDyStreamCall.queryRoomStreamInfo(room.roomId, time, sign, room.roomId, room.roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<DyStream?>> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(stringBaseResult: BaseResult<DyStream?>) {
                        val data = stringBaseResult.data
                        val live = data?.rtmpLive?.split("_")!!.toTypedArray()[0]
                        val high = "http://hw-tct.douyucdn.cn/live/$live.flv?uuid="
                        val low = "http://hw-tct.douyucdn.cn/live/" + live + "_900.flv?uuid="
                        room.liveStreamUriHigh = high
                        room.liveStreamUriLow = low
                        WaitDialog.dismiss()
                        startPlay(context, room)
//                        startPlay(room)
                    }

                    override fun onError(e: Throwable) {
                        TipDialog.show(R.string.no_stream_hint, WaitDialog.TYPE.ERROR, 500)
                        Log.e(TAG, "onError: " + e.message)
                    }

                    override fun onComplete() {}
                })
        } else {
            startPlay(context, room)
        }
    }

    private fun startPlay(context: Context, room: Room) {
        if (room.streamStatus == Room.LIVE_STATUS_ON) {
            Log.e(TAG, "startPlay: $IntroActivity.mPlayerBinder" )
            /*if (null != IntroActivity.mPlayerBinder && IntroActivity.mPlayerBinder?.isBinderAlive!!) {
                IntroActivity.mPlayerBinder!!.startPlay(room)
            }*/
            /*val intent = Intent(context, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("room_info", room)
            intent.putExtra("bundle", bundle)
            context.startActivity(intent)*/

            val introActivity = App.appStateWatcher.callbacks.topActivity as IntroActivity
            /*introActivity.runOnUiThread {
                introActivity.mBind?.player?.visibility = View.VISIBLE
                val player = ExoPlayer.Builder(introActivity).build()
                introActivity.mBind?.video?.player = player
                room.liveStreamUriHigh?.let { MediaItem.fromUri(it) }
                    ?.let { player.setMediaItem(it) }
                player.prepare()
                player.play()
            }*/
            val dialog = NotifyDialog<LayoutFloatVideoBinding>(introActivity, R.layout.layout_float_video, Gravity.CENTER)
            dialog.setAutoDismiss(false)
            dialog.setTouchMovable(true)
            dialog.show()
            dialog.setScrollDismissAble(false)

            val player = ExoPlayer.Builder(introActivity).build()
            dialog.mBind.video.player = player
            room.liveStreamUriHigh?.let { MediaItem.fromUri(it) }
                ?.let { player.setMediaItem(it) }
            player.prepare()
            player.play()

            dialog.setOnKeyListener { _, keyCode, _->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    player.stop()
                    player.release()
                    dialog.dismiss()
                    return@setOnKeyListener true
                }
                false
            }
            dialog.mBind.close.setOnClickListener {
                player.stop()
                player.release()
                dialog.dismiss()
            }

//            introActivity.mBind?.videoView.start()
//            mYouTuDraggingView.show()
//            mVideoView.setUp("https://github.com/moyokoo/Media/blob/master/Azshara.mp4?raw=true")
        } else {
            TipDialog.show(R.string.no_stream_hint, WaitDialog.TYPE.WARNING, 1500)
        }
    }

    fun indexOfRoomId(roomId: String?): Int {
        if (data.size == 0) return -1
        for (i in data.indices) {
            if (roomId == data[i].roomId) {
                return i
            }
        }
        return -1
    }

}