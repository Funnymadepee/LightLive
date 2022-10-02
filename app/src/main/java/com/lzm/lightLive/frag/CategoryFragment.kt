package com.lzm.lightLive.frag

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lzm.lib_base.BaseFreshFragment
import com.lzm.lightLive.R
import com.lzm.lightLive.adapter.CateAdapter
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.http.bean.dy.DyCate
import com.lzm.lightLive.http.bean.dy.DySortRoom
import com.lzm.lightLive.http.request.dy.DyHttpRequest
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CategoryFragment : BaseFreshFragment<DySortRoom?, CateAdapter>() {

    companion object {
        private const val TAG = "CategoryFragment"
    }

    private var mAdapter: CateAdapter? = null
    private var platform = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (null != bundle) {
            platform = bundle.getInt("platform")
        }
    }

    override fun getData() {
        if (pageNum == 0) {
            requestSubHostInfo(platform)
        } else setRefresh(false)
    }

    override fun onRefresh() {
        requestSubHostInfo(platform)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = CateAdapter(R.layout.layout_item_cate)
        setAdapter(mAdapter)
        onRefresh()
        mAdapter!!.setEnableLoadMore(false)
    }

    override fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 3)
    }

    private fun requestSubHostInfo(platform: Int) {
        mAdapter!!.data.clear()
        val dyHttpRequest = RetrofitManager.dyRetrofit.create(
            DyHttpRequest::class.java
        )
        if (platform == Room.LIVE_PLAT_DY) {
            dyHttpRequest.queryAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DyCate?> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(result: DyCate) {
                        if (mAdapter != null
                            && null != result.data
                        ) {
                            result.data?.let { mAdapter?.addData(it) }
                            setRefresh(false)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onError: " + e.message)
                    }

                    override fun onComplete() { }
                })
        }
    }

}