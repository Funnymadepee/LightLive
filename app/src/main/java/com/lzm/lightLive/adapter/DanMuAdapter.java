package com.lzm.lightLive.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzm.lib_base.util.FasterLinearSmoothScroller;
import com.lzm.lightLive.R;
import com.lzm.lightLive.http.danmu.basic.DanMu;
import com.lzm.lightLive.http.danmu.basic.DanMuMessageType;
import com.lzm.lightLive.util.AnimUtil;
import com.lzm.lightLive.util.UiTools;

public class DanMuAdapter extends BaseQuickAdapter<DanMu, BaseViewHolder>
        implements ScrollEvent {

    private static final String TAG = "DanMuAdapter";

    private final OnScrollListener scrollListener;

    private final RecyclerView mRecyclerView;

    private final View footerView;

    private FasterLinearSmoothScroller mScroller;

    public DanMuAdapter(RecyclerView recyclerView, int layoutResId, View footerView) {
        super(layoutResId);
        this.mRecyclerView = recyclerView;
        this.scrollListener = new OnScrollListener(this);
        this.footerView = footerView;
        recyclerView.setAdapter(this);

        mScroller = new FasterLinearSmoothScroller(recyclerView.getContext(), LinearSmoothScroller.SNAP_TO_ANY);
        mScroller.setTargetPosition(getItemCount() - 1);
        FasterLinearSmoothScroller.setTime(15f);

        footerView.setOnClickListener(v -> {
            mScroller.setTargetPosition(getItemCount() - 1);
            if(null != mRecyclerView.getLayoutManager()) {
                mRecyclerView.getLayoutManager().startSmoothScroll(mScroller);
            }
        });
    }

    @Override
    public void addData(@NonNull DanMu data) {
        super.addData(data);
        if(!isFooterViewShow()) {
            mRecyclerView.smoothScrollToPosition(getItemCount() - 1);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, DanMu item) {
        if (null == item) return;
        if (null == item.getUserIfo()) return;
        if (null == item.getDanMuFormatData()) return;

        String nickName = item.getUserIfo().getNickName();
        String badge = item.getUserIfo().getBadge();
        String badgeLevel = item.getUserIfo().getBadgeLevel();
        int level = item.getUserIfo().getLevel();
        boolean isBlackList = item.getUserIfo().isBlackList();
        boolean isVip = item.getUserIfo().isVip();

        int fontColor = item.getDanMuFormatData().getFontColor();

        SpannableString string = new SpannableString(nickName + " : " + item.getContent());
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")),
                0, nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#03A9F4")),
                nickName.length(), nickName.length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(fontColor),
                nickName.length() + 3, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.setText(R.id.tv_content, string);
        if(!TextUtils.isEmpty(badge)) {
            holder.setText(R.id.tv_badge, badge)
                    .setText(R.id.tv_badge_level,String.valueOf(badgeLevel))
                    .setVisible(R.id.layout_badge, true);
        }
        if(level > 0) {
            holder.setText(R.id.tv_level, String.valueOf(level))
                    .getView(R.id.layout_level).setVisibility(View.VISIBLE);
        }else {
            holder.getView(R.id.layout_level).setVisibility(View.GONE);
        }
        holder.getView(R.id.ll_normal).setVisibility(item.getUserIfo().isBlackList() ? View.GONE : View.VISIBLE);
        holder.getView(R.id.ll_black_vip).setVisibility(isBlackList ? View.VISIBLE : View.GONE);

        if(isVip) {
            holder.getView(R.id.fl_item_root).startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_anim_vip));
            holder.getView(R.id.fl_item_root).setBackground(holder.itemView.getContext().getDrawable(R.drawable.shape_douyu_vip));
        }else {
            holder.getView(R.id.fl_item_root).clearAnimation();
            holder.getView(R.id.fl_item_root).setBackgroundColor(holder.itemView.getContext().getColor(R.color.tran));
        }
    }

    @Override
    public void onTheBottom() {
        showFooterView(View.GONE);
    }

    @Override
    public void onScrolled() {
        showFooterView(View.VISIBLE);
    }

    static class OnScrollListener extends RecyclerView.OnScrollListener {
        private final ScrollEvent scrollEvent;

        public OnScrollListener(ScrollEvent scrollEvent) {
            this.scrollEvent = scrollEvent;
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {                //当前状态为停止滑动
                if(!recyclerView.canScrollVertically(1)) {          // 到达底部
                    if(null != scrollEvent) {
                        scrollEvent.onTheBottom();
                    }
                }
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(dy < 0 && (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_SETTLING)) {
                if(null != scrollEvent) {
                    scrollEvent.onScrolled();
                }
            }
        }
    }

    public OnScrollListener getScrollListener() {
        return scrollListener;
    }

    private void showFooterView(int visible) {
        if(footerView.getVisibility() != visible) {
            footerView.setVisibility(visible);
            int[] v = {1, 0};
            float[] values = (visible != View.VISIBLE)? new float[]{0f, footerView.getHeight()} : new float[]{footerView.getHeight(), 0f};
            AnimUtil.translationYAnim(footerView, 500, values);
        }
    }

    private boolean isFooterViewShow() {
        return footerView.getVisibility() == View.VISIBLE;
    }
}
