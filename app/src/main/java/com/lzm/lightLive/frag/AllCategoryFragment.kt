package com.lzm.lightLive.frag

import android.os.Bundle
import android.util.Pair
import android.view.*
import com.lzm.lib_base.BaseScrollableFragment
import com.lzm.lightLive.R
import com.lzm.lightLive.http.bean.Room
import com.lzm.lightLive.util.ResourceUtil
import com.lzm.lightLive.util.UiTools

class AllCategoryFragment : BaseScrollableFragment<CategoryFragment?>() {
    override fun getTabsName(): Array<String> {
        return arrayOf(
            getString(R.string.DouYu),
            getString(R.string.HuYa),
            getString(R.string.BiliBili)
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

    override fun initFragments(): MutableList<Pair<Int, CategoryFragment?>> {
        val dyCate = Pair(0, generateFragment(Room.LIVE_PLAT_DY))
        val hyCate = Pair(1, generateFragment(Room.LIVE_PLAT_HY))
        val blCate = Pair(2, generateFragment(Room.LIVE_PLAT_BL))
        val fragments: MutableList<Pair<Int, CategoryFragment?>> = ArrayList()
        fragments.add(dyCate)
        fragments.add(hyCate)
        fragments.add(blCate)
        return fragments
    }

    private fun generateFragment(platform: Int): CategoryFragment {
        val bundle = Bundle()
        bundle.putInt("platform", platform)
        val fragment = CategoryFragment()
        fragment.arguments = bundle
        return fragment
    }
}