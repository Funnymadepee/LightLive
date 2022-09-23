package com.lzm.lib_base;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.graphics.Color;
import androidx.lifecycle.ViewModel;
import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.appcompat.app.AppCompatActivity;
import com.lzm.lib_base.databinding.ActivityBaseBindingBinding;
import com.lzm.lib_base.status.StatusBarHost;
import com.lzm.lib_base.status.StatusBarHostLayout;

public abstract class BaseBindingActivity<VB extends ViewDataBinding, VM extends ViewModel> extends AppCompatActivity {

    protected VB mBind;
    protected VM mViewModel;
//    private ActivityBaseBindingBinding baseBind;
    protected StatusBarHostLayout statusBar;

    protected abstract @LayoutRes int getLayoutResId();
    protected abstract VM getViewModel();
    protected abstract void setViewModel(VM viewModel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBar = StatusBarHost.inject(this);
        mBind =  DataBindingUtil.setContentView(this, getLayoutResId());
        statusBar.setStatusBarImmersive(true);
        mViewModel = getViewModel();
        setViewModel(mViewModel);
        mBind.setLifecycleOwner(this);
    }
}