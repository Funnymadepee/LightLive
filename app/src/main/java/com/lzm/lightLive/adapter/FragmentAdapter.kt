package com.lzm.lightLive.adapter

import android.util.Pair
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter : FragmentStateAdapter {
    private val fragments = SparseArray<Fragment>()

    @SafeVarargs
    constructor(fragment: Fragment, vararg args: Pair<Int, Fragment>) : super(fragment) {
        for (pair in args) {
            fragments.put(pair.first!!, pair.second)
        }
    }

    @SafeVarargs
    constructor(fragmentActivity: FragmentActivity, vararg args: Pair<Int, Fragment>) : super(
        fragmentActivity
    ) {
        for (pair in args) {
            fragments.put(pair.first!!, pair.second)
        }
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }
}