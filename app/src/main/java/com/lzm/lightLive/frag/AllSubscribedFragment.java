package com.lzm.lightLive.frag;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lzm.lightLive.R;
import com.lzm.lightLive.adapter.FragmentAdapter;
import com.lzm.lightLive.http.bean.Room;

public class AllSubscribedFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentAdapter mFragmentAdapter;

    protected int getLayoutId() {
        return R.layout.fragment_subscribe_viewpager;
    }

    String[] tabs = new String[] {
            "直播中", "未开播", "直播回放"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        Pair<Integer, Fragment> on = new Pair<>(0, new SubscribeFragment(Room.LIVE_STATUS_ON));
        Pair<Integer, Fragment> off = new Pair<>(1, new SubscribeFragment(Room.LIVE_STATUS_OFF));
        Pair<Integer, Fragment> replay = new Pair<>(2, new SubscribeFragment(Room.LIVE_STATUS_REPLAY));
        mFragmentAdapter = new FragmentAdapter(this, on, off, replay);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab);
        viewPager.setAdapter(mFragmentAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager,
                true, true,
                (tab, position) -> tab.setText(tabs[position]));
        mediator.attach();

    }

}
