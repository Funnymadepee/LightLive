package com.lzm.lib_base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzm.lib_base.util.FasterLinearSmoothScroller;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFreshFragment<T, A extends BaseQuickAdapter> extends Fragment
        implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener{

    protected int pageNum;

    protected ArrayList<T> dataList;

    protected A baseAdapter;

    private SwipeRefreshLayout mSwipeLayout;

    private RecyclerView mRecyclerView;

    protected int getLayoutId() {
        return R.layout.fragment_fresh_base;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }
    protected void setRefresh(boolean refresh) {
        mSwipeLayout.setRefreshing(refresh);
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager(Context context);

    private void initView(View view) {
        dataList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recycle);
        mSwipeLayout = view.findViewById(R.id.swipe_fresh);
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light
        );
        mSwipeLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(getLayoutManager(view.getContext()));
    }

    protected RecyclerView getBaseRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 设置适配器 在子类中调用
     */
    protected void setAdapter(A adapter) {
        baseAdapter = adapter;
        baseAdapter.setOnItemClickListener(this);

        baseAdapter.setOnLoadMoreListener(this, mRecyclerView);
        baseAdapter.setEnableLoadMore(true);
        baseAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(baseAdapter);

    }

    protected void dealData(List<T> list){
        if (pageNum == 0) {
            mSwipeLayout.setRefreshing(false);
        }

        if (list != null) {
            dataList.addAll(list);
        }

        baseAdapter.notifyItemRangeChanged(0, dataList.size());

        if (list == null) {
            mSwipeLayout.setRefreshing(false);
            baseAdapter.loadMoreComplete();
            return;
        }
        baseAdapter.loadMoreComplete();
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        getData();
    }

    @Override
    public void onLoadMoreRequested() {
        this.baseAdapter.notifyItemChanged(dataList.size()-1);
        ++pageNum;
        getData();
    }

    protected abstract void getData();

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    public void scrollTo(int position){
        if(getActivity() != null
                && mRecyclerView.getLayoutManager() != null){
            FasterLinearSmoothScroller scroller =
                    new FasterLinearSmoothScroller(getActivity(), LinearSmoothScroller.SNAP_TO_START);
            scroller.setTargetPosition(position);
            scroller.setTime(30f);
            mRecyclerView.getLayoutManager().startSmoothScroll(scroller);
        }
    }
}
