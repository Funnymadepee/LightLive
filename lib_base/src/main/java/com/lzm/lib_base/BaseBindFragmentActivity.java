package com.lzm.lib_base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import com.lzm.lib_base.status.StatusBarHost;
import com.lzm.lib_base.status.StatusBarHostLayout;

public abstract class BaseBindFragmentActivity<VB extends ViewDataBinding
        , VM extends ViewModel> extends FragmentActivity {

    public VB mBind;
    public VM mViewModel;

    public abstract @LayoutRes int getLayoutResId();
    public abstract VM getViewModel();
    public abstract void setViewModel(VM viewModel);

    public StatusBarHostLayout statusBarHostLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarHostLayout = StatusBarHost.inject(this);
        statusBarHostLayout.setStatusBarImmersive(true);
        statusBarHostLayout.setStatusBarBlackText();
        mBind = DataBindingUtil.setContentView(this, getLayoutResId());
        mViewModel = getViewModel();
        setViewModel(mViewModel);
        mBind.setLifecycleOwner(this);
    }

}