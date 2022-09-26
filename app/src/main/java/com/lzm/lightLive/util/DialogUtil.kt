package com.lzm.lightLive.util

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.kongzue.dialogx.dialogs.FullScreenDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.lzm.lightLive.R
import com.lzm.lightLive.adapter.HostAdapter
import com.lzm.lightLive.common.Const
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.bean.dy.DyCate.DyCateData
import com.lzm.lightLive.http.bean.dy.DySortRoom
import com.lzm.lightLive.http.request.dy.DyHttpRequest
import com.lzm.lightLive.view.RoundImageView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object DialogUtil {
    private const val TAG = "DialogUtil"
    fun showHostInfoDialog(roomInfo: Room?) {
        FullScreenDialog.show(object : OnBindView<FullScreenDialog?>(R.layout.layout_dialog_host) {
            override fun onBind(dialog: FullScreenDialog?, v: View) {
                val avatar = v.findViewById<RoundImageView>(R.id.iv_avatar)
                val hostName = v.findViewById<AppCompatTextView>(R.id.tv_host_name)
                val roomName = v.findViewById<AppCompatTextView>(R.id.tv_room_name)
                val roomId = v.findViewById<AppCompatTextView>(R.id.tv_room_id)
                val cateName = v.findViewById<AppCompatTextView>(R.id.tv_cate_name)
                val roomUrl = v.findViewById<AppCompatTextView>(R.id.tv_room_url)
                val liveUri = v.findViewById<AppCompatTextView>(R.id.tv_live_uri)
                val animView = v.findViewById<LottieAnimationView>(R.id.animView)
                Glide.with(v)
                    .load(roomInfo?.hostAvatar)
                    .into(avatar)
                hostName.text = roomInfo?.hostName
                roomName.text = roomInfo?.roomName
                roomId.text = roomInfo?.roomId
                cateName.text = roomInfo?.cateName
                var url = ""
                when (roomInfo?.platform) {
                    Room.LIVE_PLAT_DY -> url =
                        "https://www.douyu.com/" + roomInfo.roomId
                    Room.LIVE_PLAT_HY -> url =
                        "https://www.huya.com/" + roomInfo.roomId
                    Room.LIVE_PLAT_BL -> url =
                        "https://live.bilibili.com/" + roomInfo.roomId
                }
                val string = SpannableString(url)
                string.setSpan(UnderlineSpan(), 0, string.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                string.setSpan(
                    ForegroundColorSpan(Color.parseColor("#1144aa")),
                    0, string.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                roomUrl.text = string
                roomUrl.setOnClickListener {
                    if (!TextUtils.isEmpty(roomUrl.text)) {
                        val uri = Uri.parse(roomUrl.text.toString())
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        roomUrl.context.startActivity(intent)
                    }
                }
                liveUri.text = roomInfo?.liveStreamUriHigh
                animView.setAnimation(R.raw.blue_bg)
                animView.speed = 2f
                animView.playAnimation()
            }
        })
    }

    fun showCateInfoDialog(cateData: DyCateData) {
        FullScreenDialog.show(object : OnBindView<FullScreenDialog?>(R.layout.layout_dialog_cate) {
            override fun onBind(dialog: FullScreenDialog?, v: View) {
                val tvCateName = v.findViewById<TextView>(R.id.tv_cate_name)
                val recyclerView = v.findViewById<RecyclerView>(R.id.ry_cate)
                recyclerView.layoutManager = GridLayoutManager(v.context, 2)
                tvCateName.text = cateData.cateName
                val roomList: List<Room> = ArrayList()
                val mAdapter = HostAdapter(R.layout.layout_item_host, roomList)
                mAdapter.pageMaxItem = 50
                recyclerView.adapter = mAdapter
                val call = RetrofitManager.dyRetrofit.create(
                    DyHttpRequest::class.java
                )
                call.queryCateHosts(cateData.cateId, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseResult<DySortRoom?>> {
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(result: BaseResult<DySortRoom?>) {
                            if (null != result.data) {
                                val roomList = ArrayList<Room?>()
                                for (room in result.data?.relatedList!!) {
                                    val temp =
                                        Room(room.roomId.toString(), Room.LIVE_PLAT_DY)
                                    temp.roomId = room.roomId.toString()
                                    temp.cateName = room.cateName
                                    temp.heatNum = room.online
                                    temp.streamStatus = Room.LIVE_STATUS_ON
                                    temp.roomName = room.roomName
                                    temp.hostName = room.nickName
                                    var prefix = ".jpg"
                                    if (!TextUtils.isEmpty(room.avatar)
                                        && room.avatar!!.contains("avatar")
                                        || room.avatar!!.contains("avanew")
                                    ) {
                                        prefix = "_big.jpg"
                                        temp.hostAvatar =
                                            Const.AVATAR_URL_DY + room.avatar + prefix
                                    }
                                    if (!TextUtils.isEmpty(room.thumb)) {
                                        temp.thumbUrl = room.thumb
                                    }
                                    roomList.add(temp)
                                }
                                mAdapter.setMassiveData(roomList)
                            }
                        }

                        override fun onError(e: Throwable) {
                            Log.e(TAG, "onError: " + e.message)
                        }

                        override fun onComplete() {}
                    })
            }
        })
    }
}