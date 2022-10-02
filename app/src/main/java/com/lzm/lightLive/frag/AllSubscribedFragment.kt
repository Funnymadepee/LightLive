package com.lzm.lightLive.frag

import android.os.Bundle
import android.util.Pair
import android.view.View
import com.lzm.lib_base.BaseScrollableFragment
import com.lzm.lightLive.R
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.util.ResourceUtil
import com.lzm.lightLive.util.UiTools

class AllSubscribedFragment : BaseScrollableFragment<SubscribeFragment?>() {

    override fun getTabsName(): Array<String> {
        return arrayOf(
            getString(R.string.live_status_on),
            getString(R.string.live_status_off),
            getString(R.string.live_status_replay)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabBackgroundColor(ResourceUtil.attrColor(view.context, R.attr.basic_tab_background))
        setTabTextColors(
            ResourceUtil.attrColor(view.context, R.attr.basic_tab_text),
            ResourceUtil.attrColor(view.context, R.attr.basic_tab_text_selected)
        )
        setSelectedTabIndicatorColor(
            ResourceUtil.attrColor(view.context, R.attr.basic_tab_indicator_color)
        )
        setTabRippleColor(UiTools.rippleColorStateList)
    }

    override fun initFragments(): MutableList<Pair<Int, SubscribeFragment?>> {
        val on = Pair(0, generateFragment(Room.LIVE_STATUS_ON))
        val off = Pair(1, generateFragment(Room.LIVE_STATUS_OFF))
        val replay = Pair(2, generateFragment(Room.LIVE_STATUS_REPLAY))
        val fragments: MutableList<Pair<Int, SubscribeFragment?>> = ArrayList()
        fragments.add(on)
        fragments.add(off)
        fragments.add(replay)
        return fragments
    }

    private fun generateFragment(liveStatus: Int): SubscribeFragment {
        val bundle = Bundle()
        bundle.putInt("liveStatus", liveStatus)
        val fragment = SubscribeFragment()
        fragment.arguments = bundle
        return fragment
    }
}