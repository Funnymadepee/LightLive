package com.lzm.lib_base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.lzm.lib_base.databinding.ActivityBaseBindingBinding;

public abstract class BaseBindFragmentActivity<VB extends ViewDataBinding, VM extends ViewModel> extends FragmentActivity {

    protected VB mBind;
    protected VM mViewModel;
    private ActivityBaseBindingBinding baseBind;

    protected abstract @LayoutRes int getLayoutResId();
    protected abstract VM getViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseBind = DataBindingUtil.setContentView(this, R.layout.activity_base_binding);
        initWindow();
        mBind = DataBindingUtil.inflate(getLayoutInflater(), getLayoutResId(), null, false);
        mViewModel = getViewModel();
        baseBind.setViewModel(mViewModel);
        mBind.setLifecycleOwner(this);
        baseBind.rootLayout.removeAllViews();
        baseBind.rootLayout.addView(mBind.getRoot());
    }

    private void initWindow(){
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        baseBind.rootLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}