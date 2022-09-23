package com.lzm.lightLive.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzm.lightLive.R;
import com.lzm.lightLive.http.bean.dy.DyCate;
import com.lzm.lightLive.util.CommonUtil;
import com.lzm.lightLive.util.DialogUtil;

public class CateAdapter extends BaseQuickAdapter<DyCate.DyCateData, BaseViewHolder> {

    private static final String TAG = "CateAdapter";

    public CateAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DyCate.DyCateData item) {
        Context context = helper.itemView.getContext();

        helper.itemView.setOnClickListener(v-> {
            DialogUtil.showCateInfoDialog(item);
        });
        if(!TextUtils.isEmpty(item.getCateIcon())) {
            Glide.with(context)
                    .load(item.getCateIcon())
                    .into((ImageView) helper.getView(R.id.iv_cate_icon));
        }

        helper.setText(R.id.tv_cate_name, item.getCateName())
                .setText(R.id.tv_heat_num, CommonUtil.convertInt2K(item.getCateHeat()))
                .setText(R.id.tv_cate_url, "https://www.douyu.com/" + item.getCateUrl());
    }

}
