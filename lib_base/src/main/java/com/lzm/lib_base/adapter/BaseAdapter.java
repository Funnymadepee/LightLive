package com.lzm.lib_base.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private int pageMaxItem = 30;
    private boolean pageEnable = false;

    public List<T> cachedList = new ArrayList<>();

    public BaseAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    @Override
    public void addData(@NonNull Collection<? extends T> newData) {
        if (newData.size() > pageMaxItem && pageEnable) {
            int cachedSize = cachedList.size();
            cachedList.addAll(cachedSize, newData);
            addData(cachedList.subList(cachedSize, cachedSize + pageMaxItem));
        } else {
            super.addData(newData);
        }
    }

    public int getPageMaxItem() {
        return pageMaxItem;
    }

    public void setPageMaxItem(int pageMaxItem) {
        this.pageMaxItem = pageMaxItem;
    }

    public boolean isPageEnable() {
        return pageEnable;
    }

    public void setPageEnable(boolean pageEnable) {
        this.pageEnable = pageEnable;
    }

    public List<T> getCachedList() {
        return cachedList;
    }
}
