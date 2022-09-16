package com.lzm.lightLive.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lzm.lightLive.R;
import com.lzm.lightLive.http.request.dy.DyConnect;

import java.util.List;

public class BulletScreenAdapter extends RecyclerView.Adapter<BulletScreenAdapter.ViewHolder> {

    private static final String TAG = "MyAdapter";

    private List<DyConnect.DouYuDanMu> dataList;

    Animation scaleAni = new ScaleAnimation(0.5f, 1.0f, 1.0f, 1.0f);


    public BulletScreenAdapter(List<DyConnect.DouYuDanMu> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(List<DyConnect.DouYuDanMu> danmuList) {
        int oldSize = dataList.size();
        dataList = danmuList;
        notifyItemRangeChanged(oldSize, dataList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_dm, parent, false);
        scaleAni.setDuration(500);
        scaleAni.setFillAfter(true);
        scaleAni.setRepeatCount(1);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DyConnect.DouYuDanMu dm = dataList.get(position);
        SpannableString string = new SpannableString(dm.getNn() + " : " + dm.getTxt());
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")),
                0, dm.getNn().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")),
                dm.getNn().length(), dm.getNn().length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(Color.LTGRAY),
                dm.getNn().length() + 3, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tvContent.setText(string);
        if(!TextUtils.isEmpty(dm.getBnn())) {
            holder.tvBadge.setText(dm.getBnn());
            holder.tvBadgeLevel.setText(String.valueOf(dm.getBl()));
            holder.layoutBadge.setVisibility(View.VISIBLE);
        }
        if(dm.getLevel() > 0) {
            holder.tvLevel.setText(String.valueOf(dm.getLevel()));
            holder.layoutLevel.setVisibility(View.VISIBLE);
        }else {
            holder.layoutLevel.setVisibility(View.GONE);
        }
        if(dm.isBlackVip()) {
            holder.llNormal.setVisibility(View.GONE);
            holder.llBlackVip.setVisibility(View.VISIBLE);
        }else {
            holder.llNormal.setVisibility(View.VISIBLE);
            holder.llBlackVip.setVisibility(View.GONE);
        }
        if(dm.isVip()) {
            holder.flItemRoot.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_anim_vip));
            holder.flItemRoot.setBackground(holder.itemView.getContext().getDrawable(R.drawable.shape_douyu_vip));
        }else {
            holder.flItemRoot.clearAnimation();
            holder.flItemRoot.setBackgroundColor(holder.itemView.getContext().getColor(R.color.tran));
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvBadge;
        private final TextView tvBadgeLevel;
        private final TextView tvLevel;
        private final TextView tvContent;
        private final LinearLayout llNormal;
        private final LinearLayout llBlackVip;
        private final View layoutLevel;
        private final View layoutBadge;
        private final FrameLayout flItemRoot;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvBadge = itemView.findViewById(R.id.tv_badge);
            tvBadgeLevel = itemView.findViewById(R.id.tv_badge_level);
            tvLevel = itemView.findViewById(R.id.tv_level);
            layoutLevel = itemView.findViewById(R.id.layout_level);
            layoutBadge = itemView.findViewById(R.id.layout_badge);
            llNormal = itemView.findViewById(R.id.ll_normal);
            llBlackVip = itemView.findViewById(R.id.ll_black_vip);
            flItemRoot = itemView.findViewById(R.id.fl_item_root);
        }
    }
}
