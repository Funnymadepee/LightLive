package com.lzm.lib_base;

import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.lzm.lib_base.view.DispatchKeyEventListener;

import java.util.EventListener;
import java.util.List;

public class FragmentAdapter<BF extends BaseFreshFragment> extends FragmentStateAdapter {

    private final SparseArray<Fragment> fragments = new SparseArray<>();


    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public FragmentAdapter(@NonNull Fragment fragment, List<Pair<Integer, BF>> args) {
        super(fragment);
        for(Pair<Integer, BF> pair : args) {
            if(null != pair) {
                fragments.put(pair.first, pair.second);
            }
        }
    }

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
