package com.lzm.lightLive.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseViewHolder
import com.lzm.lib_base.adapter.BaseAdapter
import com.lzm.lightLive.R
import com.lzm.lightLive.http.bean.dy.DyCate.DyCateData
import com.lzm.lightLive.util.CommonUtil
import com.lzm.lightLive.util.DialogUtil

class CateAdapter(layoutResId: Int) : BaseAdapter<DyCateData>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: DyCateData) {
        val context = helper.itemView.context
        helper.itemView.setOnClickListener { DialogUtil.showCateInfoDialog(item) }
        if (!TextUtils.isEmpty(item.cateIcon)) {
            Glide.with(context)
                .load(item.cateIcon)
                .into(helper.getView<View>(R.id.iv_cate_icon) as ImageView)
        }
        helper.setText(R.id.tv_cate_name, item.cateName)
            .setText(R.id.tv_heat_num, CommonUtil.convertInt2K(item.cateHeat))
            .setText(R.id.tv_cate_url, "https://www.douyu.com/" + item.cateUrl)
    }

    companion object {
//        private const val TAG = "CateAdapter"
    }
}