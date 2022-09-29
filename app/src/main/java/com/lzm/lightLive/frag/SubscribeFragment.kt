package com.lzm.lightLive.frag

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lzm.lib_base.BaseFreshFragment
import com.lzm.lightLive.R
import com.lzm.lightLive.adapter.HostAdapter
import com.lzm.lightLive.common.Const
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.bean.dy.DyRoom
import com.lzm.lightLive.http.bean.hy.HyRoom
import com.lzm.lightLive.http.request.dy.DyHttpRequest
import com.lzm.lightLive.http.request.hy.HyHttpRequest
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SubscribeFragment : BaseFreshFragment<Room?, HostAdapter>() {

    companion object {
        private const val TAG = "SubscribeFragment"
    }

    private var mAdapter: HostAdapter? = null
    private var showStatus = -1
    var roomList: List<Room>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (null != bundle) {
            showStatus = bundle.getInt("liveStatus")
        }
    }

    override fun getData() {
        if (pageNum == 0) {
            requestSubHostInfo(Const.subscribeHost)
        } else setRefresh(false)
    }

    override fun onRefresh() {
        //todo grab form database
        requestSubHostInfo(Const.subscribeHost)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomList = ArrayList()
        mAdapter = HostAdapter(R.layout.layout_item_host, roomList)
        setAdapter(mAdapter)
        onRefresh()
        mAdapter!!.setEnableLoadMore(false)
    }

    override fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }

    private fun requestSubHostInfo(roomPair: List<Room?>?) {
        for (pair in roomPair!!) {
            when (pair?.platform) {
                Room.LIVE_PLAT_DY -> {
                    val mDyCall = RetrofitManager.dyOpenRetrofit.create(
                        DyHttpRequest::class.java
                    )
                    mDyCall.queryRoomInfo(pair.roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<BaseResult<DyRoom?>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onNext(result: BaseResult<DyRoom?>) {
                                if (mAdapter != null
                                    && null != result.data
                                ) {
                                    if (result.data?.streamStatus == showStatus) {
                                        val position =
                                            mAdapter!!.indexOfRoomId(result.data?.roomId)
                                        if (position == -1) {
                                            mAdapter!!.addData(result.data!!)
                                        } else {
                                            mAdapter!!.setData(position, result.data!!)
                                        }
                                    }
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
                                    if (result.data!!.streamStatus == showStatus) {
                                        val position =
                                            mAdapter!!.indexOfRoomId(result.data!!.roomId)
                                        if (position == -1) {
                                            mAdapter!!.addData(result.data!!)
                                        } else {
                                            mAdapter!!.setData(position, result.data!!)
                                        }
                                    }
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

}