package com.lzm.lightLive.adapter;

import android.util.Pair;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {

    private final SparseArray<Fragment> fragments = new SparseArray<>();

    @SafeVarargs
    public FragmentAdapter(@NonNull Fragment fragment, Pair<Integer, Fragment>... args) {
        super(fragment);
        for(Pair<Integer, Fragment> pair : args) {
            if(null != pair) {
                fragments.put(pair.first, pair.second);
            }
        }
    }

    @SafeVarargs
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity, Pair<Integer, Fragment>... args) {
        super(fragmentActivity);
        for(Pair<Integer, Fragment> pair : args) {
            if(null != pair) {
                fragments.put(pair.first, pair.second);
            }
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
