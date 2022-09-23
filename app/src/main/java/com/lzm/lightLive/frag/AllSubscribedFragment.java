package com.lzm.lightLive.frag;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.lzm.lib_base.BaseScrollableFragment;
import com.lzm.lightLive.R;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.util.ResourceUtil;
import com.lzm.lightLive.util.UiTools;

import java.util.ArrayList;
import java.util.List;

public class AllSubscribedFragment extends BaseScrollableFragment {

    @Override
    protected String[] getTabsName() {
        return new String[] {
                getString(R.string.live_status_on),
                getString(R.string.live_status_off),
                getString(R.string.live_status_replay),
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
    protected List<Pair<Integer, SubscribeFragment>> initFragments() {
        Pair<Integer, SubscribeFragment> on = new Pair<>(0, generateFragment(Room.LIVE_STATUS_ON));
        Pair<Integer, SubscribeFragment> off = new Pair<>(1, generateFragment(Room.LIVE_STATUS_OFF));
        Pair<Integer, SubscribeFragment> replay = new Pair<>(2, generateFragment(Room.LIVE_STATUS_REPLAY));
        List<Pair<Integer, SubscribeFragment>> fragments = new ArrayList<>();
        fragments.add(on);
        fragments.add(off);
        fragments.add(replay);
        return fragments;
    }

    private SubscribeFragment generateFragment(int liveStatus) {
        Bundle bundle = new Bundle();
        bundle.putInt("liveStatus", liveStatus);
        SubscribeFragment fragment = new SubscribeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
