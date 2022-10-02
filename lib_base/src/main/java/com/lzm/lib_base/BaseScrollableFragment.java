package com.lzm.lib_base;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.List;

public abstract class BaseScrollableFragment<BF extends BaseFreshFragment > extends Fragment {

    protected TabLayout tabLayout;
    protected ViewPager2 viewPager;
    protected FragmentAdapter<BF> mFragmentAdapter;

    protected abstract String[] getTabsName();

    protected abstract List<Pair<Integer, BF>> initFragments();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewpager, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mFragmentAdapter = new FragmentAdapter(this, initFragments());
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab);
        viewPager.setAdapter(mFragmentAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager,
                true, true,
                (tab, position) -> tab.setText(getTabsName()[position]));
        mediator.attach();
    }

    public void scrollToTop() {
        try {
            int currentItem = viewPager.getCurrentItem();
            BaseFreshFragment fragment = (BaseFreshFragment) getChildFragmentManager().findFragmentById(currentItem);
            fragment.scrollTo(0);
        }catch (NullPointerException | ClassCastException e) {
            //ignore.
        }
    }

    public void setTabBackgroundResource(@DrawableRes int drawableRes) {
        if (null != tabLayout) {
            tabLayout.setBackgroundResource(drawableRes);
        }
    }

    public void setTabBackgroundColor(@ColorInt int color) {
        if (null != tabLayout) {
            tabLayout.setBackgroundColor(color);
        }
    }

    public void setTabTextColors(int textColors, int textColorSelected) {
        if (null != tabLayout) {
            tabLayout.setTabTextColors(
                    textColors,
                    textColorSelected
            );
        }
    }

    public void setSelectedTabIndicatorColor(int selectedTabIndicatorColor) {
        if (null != tabLayout) {
            tabLayout.setSelectedTabIndicatorColor(selectedTabIndicatorColor);
        }
    }

    public void setTabRippleColor(ColorStateList color) {
        if (tabLayout != null) {
            tabLayout.setTabRippleColor(color);
        }
    }

}
