package com.lzm.lightLive.frag

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.lzm.lib_base.BaseFreshFragment
import com.lzm.lightLive.R
import com.lzm.lightLive.adapter.HostAdapter
import com.lzm.lightLive.common.Const
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.bean.dy.DyRoom
import com.lzm.lightLive.http.bean.dy.DySortRoom
import com.lzm.lightLive.http.bean.hy.HyRoom
import com.lzm.lightLive.http.request.dy.DyHttpRequest
import com.lzm.lightLive.http.request.hy.HyHttpRequest
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeFragment : BaseFreshFragment<Room?, HostAdapter>() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var mAdapter: HostAdapter? = null
    var roomList: List<Room> = ArrayList()
    var cacheList: List<Room> = ArrayList()
    override fun getData() {
        if (pageNum > 0) {
            requestHotHost()
        } else {
            mAdapter?.cachedList?.clear()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        requestHotHost()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        roomList = Const.getSubscribeHost();
        mAdapter = HostAdapter(R.layout.layout_item_host, roomList)
        setAdapter(mAdapter)
        onRefresh()
    }

    override fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }

    private var dyRetrofit: DyHttpRequest = RetrofitManager.dyRetrofit.create(
        DyHttpRequest::class.java
    )

    private fun requestHotHost() {
        dyRetrofit.queryLiveHeatSort(pageNum + 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResult<DySortRoom?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(result: BaseResult<DySortRoom?>) {
                    if (null != result.data) {
                        val roomList: MutableList<Room?> = ArrayList()
                        for (room in result.data!!.relatedList!!) {
                            val temp = Room(room.roomId.toString(), Room.LIVE_PLAT_DY)
                            temp.roomId = room.roomId.toString()
                            temp.cateName = room.cateName
                            temp.heatNum = room.online
                            temp.streamStatus = Room.LIVE_STATUS_ON
                            temp.roomName = room.roomName
                            temp.hostName = room.nickName
                            var prefix = ".jpg"
                            if (room.avatar!!.contains("avatar")
                                || room.avatar!!.contains("avanew")
                            ) {
                                prefix = "_big.jpg"
                            }
                            temp.hostAvatar = Const.AVATAR_URL_DY + room.avatar + prefix
                            temp.thumbUrl = room.thumb
                            roomList.add(temp)
                        }
                        Log.e(TAG, "onNext: " + pageNum + " roomList: " + roomList.size)
                        mAdapter!!.addData(roomList)
                        setRefresh(false)
                        baseAdapter.loadMoreComplete()
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: " + e.message)
                }

                override fun onComplete() {
                    Log.e(TAG, "onComplete: $pageNum")
                }
            })
    }

    private fun requestSubHostInfo(roomPair: List<Room>) {
        for (pair in roomPair) {
            when (pair.platform) {
                Room.LIVE_PLAT_DY -> {
                    val mDyCall = RetrofitManager.dyOpenRetrofit.create(DyHttpRequest::class.java)
                    mDyCall.queryRoomInfo(pair.roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<BaseResult<DyRoom?>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onNext(result: BaseResult<DyRoom?>) {
                                if (mAdapter != null
                                    && null != result.data
                                ) {
                                    mAdapter!!.addData(result.data!!)
                                    setRefresh(false)
                                }
                            }

                            override fun onError(e: Throwable) {
                                baseAdapter.loadMoreFail()
                                Log.e(TAG, "mDyCall onError: " + e.message)
                            }

                            override fun onComplete() {}
                        })
                }
                Room.LIVE_PLAT_HY -> {
                    val mHyCall = RetrofitManager.hyMpRetrofit.create(
                        HyHttpRequest::class.java
                    )
                    mHyCall.queryRoomInfo(pair.roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<BaseResult<HyRoom?>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onNext(result: BaseResult<HyRoom?>) {
                                if (mAdapter != null
                                    && null != result.data
                                ) {
                                    mAdapter!!.addData(result.data!!)
                                    setRefresh(false)
                                }
                            }

                            override fun onError(e: Throwable) {
                                baseAdapter.loadMoreFail()
                                Log.e(TAG, "mHyCall onError: " + e.message)
                            }

                            override fun onComplete() {}
                        })
                }
            }
        }
    }

    override fun onLoadMoreRequested() {
        val size = mAdapter!!.data.size
        val cachedSize = mAdapter!!.cachedList.size
        if (cachedSize > size) {
            if (cachedSize > size + mAdapter!!.pageMaxItem) {
                mAdapter?.addData(
                    size,
                    mAdapter!!.cachedList.subList(size, size + mAdapter!!.pageMaxItem)
                )
            } else {
                mAdapter?.addData(size, mAdapter!!.cachedList.subList(size, cachedSize))
            }
            baseAdapter.loadMoreComplete()
        } else {
            ++pageNum
            super.onLoadMoreRequested()
        }
    }

    fun search(text: String?): List<Room>? {
        if (null == text || "" == text) {
            //close.
            return null
        }
        val tempList: MutableList<Room> = ArrayList()
        if (null == mAdapter) return null
        for (data in mAdapter!!.data) {
            if (null != data && data.toString().contains(text)) {
                tempList.add(data)
            }
        }
        return tempList
    }

}