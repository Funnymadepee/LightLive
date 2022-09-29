package com.lzm.lightLive.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lzm.lib_base.adapter.BaseAdapter
import com.lzm.lib_base.util.FasterLinearSmoothScroller
import com.lzm.lightLive.R
import com.lzm.lightLive.http.danmu.basic.DanMu
import com.lzm.lightLive.util.AnimUtil
import com.lzm.lightLive.util.ResourceUtil

class DanMuAdapter(private val mRecyclerView: RecyclerView, layoutResId: Int, footerView: View) :
    BaseAdapter<DanMu>(layoutResId), ScrollEvent {

    companion object {
//        private const val TAG = "DanMuAdapter"
    }
    
    val scrollListener: OnScrollListener = OnScrollListener(this)
    private val footerView: View
    var isDanMuUpdate = true
    private val mScroller: FasterLinearSmoothScroller
    override fun addData(data: DanMu) {
        if (isDanMuUpdate) {
            super.addData(data)
            if (!isFooterViewShow) {
                mRecyclerView.smoothScrollToPosition(itemCount - 1)
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: DanMu) {
        if (null == item.userIfo) return
        if (null == item.danMuFormatData) return
        val context = holder.itemView.context
        val nickName = item.userIfo?.nickName
        val badge = item.userIfo?.badge
        val badgeLevel = item.userIfo?.badgeLevel
        val level = item.userIfo!!.level
        val isBlackList = item.userIfo?.isBlackList
        val isVip = item.userIfo?.isVip
        val fontColor = item.danMuFormatData!!.fontColor
        val string = SpannableString(nickName + " : " + item.content)
        //Nickname Span
        string.setSpan(
            ForegroundColorSpan(
                ResourceUtil.attrColor(context, R.attr.basic_dm_nickname_color)
            ),
            0,
            nickName!!.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        //divider : Span
        string.setSpan(
            ForegroundColorSpan(
                ResourceUtil.attrColor(context, R.attr.basic_dm_nickname_color)
            ),
            nickName.length,
            nickName.length + 3,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        //danMu text Span
        string.setSpan(
            ForegroundColorSpan(fontColor),
            nickName.length + 3, string.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        holder.setText(R.id.tv_content, string)
        if (!TextUtils.isEmpty(badge)) {
            holder.setText(R.id.tv_badge, badge)
                .setText(R.id.tv_badge_level, badgeLevel.toString())
                .setVisible(R.id.layout_badge, true)
        }
        if (level > 0) {
            holder.setText(R.id.tv_level, level.toString())
                .getView<View>(R.id.layout_level).visibility = View.VISIBLE
        } else {
            holder.getView<View>(R.id.layout_level).visibility = View.GONE
        }
        holder.getView<View>(R.id.ll_normal).visibility =
            if (item.userIfo!!.isBlackList) View.GONE else View.VISIBLE
        holder.getView<View>(R.id.ll_black_vip).visibility =
            if (isBlackList == true) View.VISIBLE else View.GONE
        if (isVip == true) {
            holder.getView<View>(R.id.fl_item_root).startAnimation(
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.item_anim_vip
                )
            )
            holder.getView<View>(R.id.fl_item_root).background =
                ResourcesCompat.getDrawable(holder.itemView.resources, R.drawable.shape_dy_vip, null)
        } else {
            holder.getView<View>(R.id.fl_item_root).clearAnimation()
            holder.getView<View>(R.id.fl_item_root)
                .setBackgroundColor(holder.itemView.context.getColor(R.color.tran))
        }
    }

    override fun onTheBottom() {
        showFooterView(View.GONE)
    }

    override fun onScrolled() {
        showFooterView(View.VISIBLE)
    }

    class OnScrollListener(private val scrollEvent: ScrollEvent?) :
        RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {                //当前状态为停止滑动
                if (!recyclerView.canScrollVertically(1)) {          // 到达底部
                    scrollEvent?.onTheBottom()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy < 0 && recyclerView.scrollState != RecyclerView.SCROLL_STATE_SETTLING) {
                scrollEvent?.onScrolled()
            }
        }
    }

    private fun showFooterView(visible: Int) {
        if (footerView.visibility != visible) {
            footerView.visibility = visible
            intArrayOf(1, 0)
            val values = if (visible != View.VISIBLE) floatArrayOf(
                0f,
                footerView.height.toFloat()
            ) else floatArrayOf(footerView.height.toFloat(), 0f)
            AnimUtil.translationYAnim(footerView, 500, *values)
        }
    }

    private val isFooterViewShow: Boolean
        get() = footerView.visibility == View.VISIBLE

    init {
        this.footerView = footerView
        mRecyclerView.adapter = this
        mScroller =
            FasterLinearSmoothScroller(mRecyclerView.context, LinearSmoothScroller.SNAP_TO_ANY)
        mScroller.targetPosition = itemCount - 1
        mScroller.setTime(30f)
        footerView.setOnClickListener {
            mScroller.targetPosition = itemCount - 1
            if (null != mRecyclerView.layoutManager) {
                mRecyclerView.layoutManager!!.startSmoothScroll(mScroller)
            }
        }
    }
}