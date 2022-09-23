package com.lzm.lib_base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import com.lzm.lib_base.status.StatusBarHost;

public abstract class BaseBindFragmentActivity<VB extends ViewDataBinding, VM extends ViewModel> extends FragmentActivity {

    protected VB mBind;
    protected VM mViewModel;

    protected abstract @LayoutRes int getLayoutResId();
    protected abstract VM getViewModel();
    protected abstract void setViewModel(VM viewModel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHost.inject(this).setStatusBarImmersive(true).setStatusBarBlackText();
        mBind = DataBindingUtil.setContentView(this, getLayoutResId());
        mViewModel = getViewModel();
        setViewModel(mViewModel);
        mBind.setLifecycleOwner(this);
    }

}