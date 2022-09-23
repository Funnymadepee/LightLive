package com.lzm.lightLive.frag;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import androidx.annotation.Nullable;
import com.lzm.lib_base.BaseScrollableFragment;
import com.lzm.lightLive.R;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.util.ResourceUtil;
import com.lzm.lightLive.util.UiTools;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryFragment extends BaseScrollableFragment {

    @Override
    protected String[] getTabsName() {
        return new String[] {
                getString(R.string.DouYu),
                getString(R.string.HuYa),
                getString(R.string.BiliBili)
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabBackgroundColor(ResourceUtil.attrColor(view.getContext(), R.attr.basic_tab_background));
        setTabTextColors(
                ResourceUtil.attrColor(view.getContext(), R.attr.basic_tab_text),
                ResourceUtil.attrColor(view.getContext(), R.attr.basic_tab_text_selected)
        );
        setSelectedTabIndicatorColor(
                ResourceUtil.attrColor(view.getContext(), R.attr.basic_tab_indicator_color)
        );
        setTabRippleColor(UiTools.getRippleColorStateList());
    }

    @Override
    protected List<Pair<Integer, CategoryFragment>> initFragments() {
        Pair<Integer, CategoryFragment> dyCate = new Pair<>(0, generateFragment(Room.LIVE_PLAT_DY));
        Pair<Integer, CategoryFragment> hyCate = new Pair<>(1, generateFragment(Room.LIVE_PLAT_HY));
        Pair<Integer, CategoryFragment> blCate = new Pair<>(2, generateFragment(Room.LIVE_PLAT_BL));

        List<Pair<Integer, CategoryFragment>> fragments = new ArrayList<>();
        fragments.add(dyCate);
        fragments.add(hyCate);
        fragments.add(blCate);
        return fragments;
    }

    private CategoryFragment generateFragment(int platform) {
        Bundle bundle = new Bundle();
        bundle.putInt("platform", platform);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
