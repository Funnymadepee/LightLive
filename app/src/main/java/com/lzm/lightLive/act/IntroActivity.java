package com.lzm.lightLive.act;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.lzm.lib_base.BaseBindFragmentActivity;
import com.lzm.lib_base.BaseFreshFragment;
import com.lzm.lightLive.R;
import com.lzm.lightLive.adapter.FragmentAdapter;
import com.lzm.lightLive.databinding.ActivityIntroBinding;
import com.lzm.lightLive.frag.AllCategoryFragment;
import com.lzm.lightLive.frag.HomeFragment;
import com.lzm.lightLive.frag.SettingFragment;
import com.lzm.lightLive.frag.AllSubscribedFragment;
import com.lzm.lightLive.util.UiTools;
import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends BaseBindFragmentActivity<ActivityIntroBinding, ViewModel>
        implements BottomNavigationView.OnItemSelectedListener {

    private static final String TAG = "IntroActivity";

    private MenuItem itemHome;
    private MenuItem itemTag;
    private MenuItem itemUser;
    private MenuItem itemSearch;

    private final List<MenuItem> menuItems = new ArrayList<>();

    private FragmentAdapter fragmentPager;

    private int appBarSlideOffset = -1;

    private float percent = 0f;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_intro;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        window.setSharedElementsUseOverlay(false);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);

        initBottomNav();
        initFragment();
        mBind.appBarLayout.setAlpha(1);
        mBind.appBarLayout.post(this::initAppBarListener);
        showFragment(0);
    }

    private void initFragment() {

        Pair<Integer, Fragment> home = new Pair<>(0, new HomeFragment());
        Pair<Integer, Fragment> cate = new Pair<>(1, new AllCategoryFragment());
        Pair<Integer, Fragment> sub = new Pair<>(2, new AllSubscribedFragment());
        Pair<Integer, Fragment> set = new Pair<>(3, new SettingFragment());

        fragmentPager = new FragmentAdapter(this, home, cate, sub, set);
        mBind.content.setOffscreenPageLimit(2);
        mBind.content.setUserInputEnabled(true);
        mBind.content.setAdapter(fragmentPager);
        mBind.content.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mBind.bottomNavigation.setSelectedItemId(menuItems.get(position).getItemId());
                super.onPageSelected(position);
            }
        });
    }

    private void initAppBarListener() {
        ViewGroup.LayoutParams barLp = mBind.actionBar.getLayoutParams();
        FrameLayout.LayoutParams tvTitleLp = (FrameLayout.LayoutParams) mBind.tvTitle.getLayoutParams();

        int barHeight = mBind.actionBar.getHeight();
        int tvTitleTopMargin = tvTitleLp.topMargin;
        int tvTitleLeftMargin = tvTitleLp.leftMargin;    //UiTools.dp2px(this, 30);

        float textSize = UiTools.pxToDp(this, mBind.tvTitle.getTextSize());

        mBind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == appBarSlideOffset) {
                return;
            }
            appBarSlideOffset = verticalOffset;
            percent = Math.abs(verticalOffset * 1.0f) / mBind.anchorView.getTop();
            if (percent != 0f) {
                barLp.height = (int) (barHeight - barHeight * (0.7 * percent));
                tvTitleLp.topMargin = (int) (tvTitleTopMargin - tvTitleTopMargin * (0.5 * percent));
                tvTitleLp.leftMargin = (int) (tvTitleLeftMargin + (tvTitleLeftMargin * percent));

                runOnUiThread(() -> {
                    mBind.actionBar.setLayoutParams(barLp);
                    mBind.tvTitle.setLayoutParams(tvTitleLp);
                    mBind.tvTitle.setTextSize((float) (textSize - (textSize * (0.3 * percent))));
                });
            }
            if (Math.abs(verticalOffset) >= mBind.anchorView.getTop()) {
//                mBind.tvTitle.setTextColor(Color.WHITE);
//                mBind.ivTitle.setTextColor(Color.WHITE);
                mBind.actionBarBlur.setVisibility(View.VISIBLE);
                UiTools.setStatusBar(this, true);
            } else {
                mBind.actionBarBlur.setVisibility(View.VISIBLE);
//                mBind.tvTitle.setTextColor(Color.WHITE);
//                DrawableCompat.setTint(mBind.ivTitle.getDrawable(), getResources().getColor(R.color.black));
                mBind.actionBarBlur.setVisibility(View.GONE);
                UiTools.setStatusBar(this, false);
            }
        });
    }

    private void initBottomNav() {
        menuItems.add(itemHome);
        menuItems.add(itemTag);
        menuItems.add(itemUser);
        menuItems.add(itemSearch);
        for (MenuItem item : menuItems) {
            @StringRes int string = -1;
            @DrawableRes int drawable = -1;
            int position = menuItems.indexOf(item);
            switch (position) {
                case 0:
                    string = R.string.menu_home;
                    drawable = R.drawable.ic_menu_home;
                    break;
                case 1:
                    string = R.string.menu_tag;
                    drawable = R.drawable.ic_menu_tag;
                    break;
                case 2:
                    string = R.string.menu_user;
                    drawable = R.drawable.ic_menu_user;
                    break;
                case 3:
                    string = R.string.menu_setting;
                    drawable = R.drawable.ic_menu_setting;
                    break;
            }
            item = initBottomNav(string, position, drawable);
            menuItems.set(position, item);
        }
        mBind.bottomNavigation.setOnItemSelectedListener(this);
    }

    private MenuItem initBottomNav(@StringRes int tag, int index, @DrawableRes int drawable) {
        mBind.bottomNavigation.getMenu().add(0, index, index, tag);
        mBind.bottomNavigation.getMenu().getItem(index).setIcon(drawable);
        return mBind.bottomNavigation.getMenu().getItem(index);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        showFragment(item.getItemId());
        return true;
    }

    int mLastSelectedPosition = -1;
    private void showFragment(int position) {
        if(position != mLastSelectedPosition) {
            changeTitle(position);
            mBind.content.setCurrentItem(position, true);
            mLastSelectedPosition = position;
        }else {
            scrollToTop(position);
        }
    }

    private long lastClickTime;
    private void scrollToTop(int position) {
        long now = System.currentTimeMillis();
        if((now - lastClickTime) < 1200) {
            Fragment fragment = fragmentPager.createFragment(position);
            if(fragment instanceof BaseFreshFragment) {
                lastClickTime = 0;
                ((BaseFreshFragment<?>) fragment).scrollTo(0);
            }
            mBind.appBarLayout.setExpanded(true, true);
        }
        lastClickTime = now;
    }

    private void changeTitle(int position) {
        String title;
        switch (position) {
            case 0:
                title = getString(R.string.menu_home);
                mBind.animationView.setAnimation(R.raw.recommend);
                break;
            case 1:
                title = getString(R.string.menu_tag);
                mBind.animationView.setAnimation(R.raw.setting);
                break;
            case 2:
                title = getString(R.string.menu_user);
                mBind.animationView.setAnimation(R.raw.myself);
                break;
            case 3:
                title = getString(R.string.menu_setting);
                mBind.animationView.setAnimation(R.raw.search);
                break;
            default:
                title = "";
                break;
        }
        mBind.animationView.setRepeatCount(LottieDrawable.INFINITE);
        mBind.animationView.playAnimation();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(mBind.tvTitle, "alpha", 1f, 0f);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(mBind.tvTitle, "alpha", 0f, 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mBind.tvTitle, "translationY"
                , mBind.tvTitle.getHeight(), 0f);
        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(mBind.tvTitle, "translationY"
                , 0f, mBind.tvTitle.getHeight());
        AnimatorSet animSet = new AnimatorSet();
        long duration = 40L;
        alpha.setDuration(duration);
        alpha2.setDuration(duration);
        translationY.setDuration(duration);
        translationY2.setDuration(duration);

        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBind.tvTitle.setText(title);
            }
        });
        animSet.setDuration(duration * 4);
        animSet.play(alpha2).after(alpha);
        animSet.play(translationY).after(translationY2);
        animSet.start();
    }

}