package com.lzm.lightLive.act

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.util.*
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.lzm.lib_base.BaseBindFragmentActivity
import com.lzm.lib_base.BaseFreshFragment
import com.lzm.lib_base.BaseScrollableFragment
import com.lzm.lib_base.behavior.BottomNavBarBehavior
import com.lzm.lightLive.R
import com.lzm.lightLive.adapter.FragmentAdapter
import com.lzm.lightLive.adapter.HostAdapter
import com.lzm.lightLive.databinding.ActivityIntroBinding
import com.lzm.lightLive.frag.AllCategoryFragment
import com.lzm.lightLive.frag.AllSubscribedFragment
import com.lzm.lightLive.frag.HomeFragment
import com.lzm.lightLive.frag.SettingFragment
import com.lzm.lightLive.http.BaseResult
import com.lzm.lightLive.http.RetrofitManager
import com.lzm.lightLive.http.bean.bl.BLRoom
import com.lzm.lightLive.http.request.bl.BlHttpRequest
import com.lzm.lightLive.model.RoomViewModel
import com.lzm.lightLive.util.CacheUtil
import com.lzm.lightLive.util.SimpleTextWatcher
import com.lzm.lightLive.util.UiTools
import com.lzm.lightLive.video.service.FloatingService
import com.lzm.lightLive.view.YouTuDraggingView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs

class IntroActivity : BaseBindFragmentActivity<ActivityIntroBinding?, ViewModel?>(), NavigationBarView.OnItemSelectedListener {

    companion object {
        private const val TAG = "IntroActivity"

        var mPlayerBinder: FloatingService.PlayerBinder? = null

        private val mConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder) {
                Log.e(TAG, "onServiceConnected: $name $service" )
                mPlayerBinder = service as FloatingService.PlayerBinder
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                mPlayerBinder = null
            }
        }
    }

    private val itemHome: MenuItem? = null
    private val itemTag: MenuItem? = null
    private val itemUser: MenuItem? = null
    private val itemSearch: MenuItem? = null
    private val menuItems: MutableList<MenuItem?> = ArrayList()
    private var fragmentPager: FragmentAdapter? = null
    private var appBarSlideOffset = -1
    private var percent = 0f

    override fun getLayoutResId(): Int {
        return R.layout.activity_intro
    }

    override fun getViewModel(): ViewModel {
        return RoomViewModel()
    }

    override fun setViewModel(viewModel: ViewModel?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val window = window
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        initView()
        initBottomNav()
        initFragment()
        initCustomSetting()
        test()
        mBind?.appBarLayout?.alpha = 1f
        mBind?.appBarLayout?.post { initAppBarListener() }
        showFragment(0)

//        val intent = Intent(this, FloatingService::class.java)
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    private fun test() {
        val mBlRequest = RetrofitManager.blRetrofit.create(
            BlHttpRequest::class.java
        )
        mBlRequest.getRoomInfo("5194110")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResult<BLRoom?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(result: BaseResult<BLRoom?>) {
                    Log.e(TAG, "getRoomInfo: ${result.data}" )

                    mBlRequest.getUidInfo(arrayOf(result.data?.uid))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<BaseResult<BLRoom?>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onNext(result: BaseResult<BLRoom?>) {
                                Log.e(TAG, "onNext: ${result.data}" )
                            }
                            override fun onError(e: Throwable) {
                                Log.e(TAG, "onError: $e" )
                            }
                            override fun onComplete() { }
                        })
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "getRoomInfo: " + e.message)
                }

                override fun onComplete() {}
            })

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        mBind?.searchRv?.layoutManager = GridLayoutManager(this, 2)
    }

    private fun initCustomSetting() {
        try {
            val blurBehavior: BottomNavBarBehavior? =
                (mBind?.actionBarBlur?.layoutParams as CoordinatorLayout.LayoutParams).behavior as BottomNavBarBehavior?
            val navBehavior: BottomNavBarBehavior? =
                (mBind?.bottomNavigation?.layoutParams as CoordinatorLayout.LayoutParams).behavior as BottomNavBarBehavior?
            if (null != blurBehavior
                && null != navBehavior
            ) {
                blurBehavior.isIsNestScroll = CacheUtil.getNavBarIsNestScroll(this)
                navBehavior.isIsNestScroll = CacheUtil.getNavBarIsNestScroll(this)
            }
        } catch (e: Exception) {
            //ignore.
        }
    }

    fun ViewPager2.bindFragment(
        fm: FragmentManager,
        lifecycle: Lifecycle,
        fragments: List<Fragment>
    ): ViewPager2 {
        offscreenPageLimit = fragments.size - 1

        adapter = object : FragmentStateAdapter(fm, lifecycle) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }
        return this
    }

    private fun initFragment() {
        mBind?.content?.bindFragment(
            supportFragmentManager,
            this.lifecycle,
            listOf(HomeFragment(), AllCategoryFragment(), AllSubscribedFragment(), SettingFragment()),
        )
        val home = Pair<Int, Fragment>(0, HomeFragment())
        val cate = Pair<Int, Fragment>(1, AllCategoryFragment())
        val sub = Pair<Int, Fragment>(2, AllSubscribedFragment())
        val set = Pair<Int, Fragment>(3, SettingFragment())
        fragmentPager = FragmentAdapter(this@IntroActivity,
            home,
            cate,
            sub,
            set
        )
        mBind?.content?.offscreenPageLimit = 2
//        mBind?.content?.isUserInputEnabled = true
        mBind?.content?.adapter = fragmentPager
        mBind?.content?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBind?.bottomNavigation?.selectedItemId = menuItems[position]!!.itemId
                super.onPageSelected(position)
            }
        })
    }

    private fun initAppBarListener() {
        val barLp = mBind?.actionBar?.layoutParams
        val tvTitleLp = mBind?.tvTitle?.layoutParams as FrameLayout.LayoutParams
        val barHeight = mBind?.actionBar!!.height
        val tvTitleTopMargin = tvTitleLp.topMargin
        val tvTitleLeftMargin = tvTitleLp.leftMargin //UiTools.dp2px(this, 30);
        val textSize = UiTools.pxToDp(this, mBind?.tvTitle!!.textSize)
        mBind?.fab?.setOnClickListener {
            if (mBind?.searchBar?.visibility == View.GONE) {
                mBind?.fab?.setImageResource(R.drawable.ic_close)
                UiTools.circleReveal(mBind?.searchBar!!, true)
            } else {
                mBind?.fab?.setImageResource(R.drawable.ic_search)
                UiTools.circleReveal(mBind?.searchBar!!, false)
                search("")
            }
            UiTools.circleReveal(mBind?.fab!!, true)
        }
        mBind?.searchBar?.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                Log.e(TAG, "afterTextChanged: $s")
                search(s.toString())
            }
        })
        mBind?.appBarLayout?.addOnOffsetChangedListener { _: AppBarLayout?, verticalOffset: Int ->
            if (verticalOffset == appBarSlideOffset) {
                return@addOnOffsetChangedListener
            }
            appBarSlideOffset = verticalOffset
            percent = abs(verticalOffset * 1.0f) / mBind?.anchorView!!.top
            if (percent != 0f) {
                barLp?.height = (barHeight - barHeight * (0.6 * percent)).toInt()
                tvTitleLp.topMargin =
                    (tvTitleTopMargin - tvTitleTopMargin * (0.5 * percent)).toInt()
                tvTitleLp.leftMargin = (tvTitleLeftMargin + tvTitleLeftMargin * percent).toInt()
                runOnUiThread {
                    mBind?.actionBar?.layoutParams = barLp
                    mBind?.tvTitle?.layoutParams = tvTitleLp
                    mBind?.tvTitle?.textSize = (textSize - textSize * (0.3 * percent)).toFloat()
                }
            }
            if (abs(verticalOffset) >= mBind?.anchorView!!.top) { //滑动到顶了
                mBind?.actionBarBlur?.alpha = 1f
            } else {                                              //滑动中 percent = 收缩比例
                mBind?.actionBarBlur?.alpha = percent
            }
        }
    }

    private fun initBottomNav() {
        menuItems.add(itemHome)
        menuItems.add(itemTag)
        menuItems.add(itemUser)
        menuItems.add(itemSearch)
        for (item in menuItems) {
            @StringRes var string = -1
            @DrawableRes var drawable = -1
            val position = menuItems.indexOf(item)
            when (position) {
                0 -> {
                    string = R.string.menu_home
                    drawable = R.drawable.ic_menu_home
                }
                1 -> {
                    string = R.string.menu_tag
                    drawable = R.drawable.ic_menu_tag
                }
                2 -> {
                    string = R.string.menu_user
                    drawable = R.drawable.ic_menu_user
                }
                3 -> {
                    string = R.string.menu_setting
                    drawable = R.drawable.ic_menu_setting
                }
            }
            menuItems[position] = initBottomNav(string, position, drawable)
        }
        mBind?.bottomNavigation?.setOnItemSelectedListener(this)
    }

    private fun initBottomNav(
        @StringRes tag: Int,
        index: Int,
        @DrawableRes drawable: Int
    ): MenuItem {
        mBind?.bottomNavigation?.menu?.add(0, index, index, tag)
        mBind?.bottomNavigation?.menu?.getItem(index)?.setIcon(drawable)
        return mBind?.bottomNavigation?.menu!!.getItem(index)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        showFragment(item.itemId)
        return true
    }

    private var mLastSelectedPosition = -1
    private fun showFragment(position: Int) {
        if (position != mLastSelectedPosition) {
            changeTitle(position)
            mBind?.content?.setCurrentItem(position, true)
            mLastSelectedPosition = position
        } else {
            scrollToTop(position)
        }
    }

    private var lastClickTime: Long = 0
    private fun scrollToTop(position: Int) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime < 1200) {
            mBind?.appBarLayout?.setExpanded(true, true)
            val fragment = fragmentPager!!.createFragment(position)
            if (fragment is BaseFreshFragment<*, *>) {
                lastClickTime = 0
                fragment.scrollTo(0)
            } else if (fragment is BaseScrollableFragment<*>) {
                lastClickTime = 0
                fragment.scrollToTop()
            }
        }else {
            lastClickTime = now
        }
    }

    private fun changeTitle(position: Int) {
        val title: String
        when (position) {
            0 -> {
                title = getString(R.string.menu_home)
                mBind?.animationView?.setAnimation(R.raw.recommend)
            }
            1 -> {
                title = getString(R.string.menu_tag)
                mBind?.animationView?.setAnimation(R.raw.setting)
            }
            2 -> {
                title = getString(R.string.menu_user)
                mBind?.animationView?.setAnimation(R.raw.myself)
            }
            3 -> {
                title = getString(R.string.menu_setting)
                mBind?.animationView?.setAnimation(R.raw.search)
            }
            else -> title = ""
        }
        mBind?.animationView!!.repeatCount = LottieDrawable.INFINITE
        mBind?.animationView!!.playAnimation()
        val alpha = ObjectAnimator.ofFloat(mBind?.tvTitle, "alpha", 1f, 0f)
        val alpha2 = ObjectAnimator.ofFloat(mBind?.tvTitle, "alpha", 0f, 1f)
        val translationY = ObjectAnimator.ofFloat(
            mBind?.tvTitle, "translationY", mBind?.tvTitle!!.height.toFloat(), 0f
        )
        val translationY2 = ObjectAnimator.ofFloat(
            mBind?.tvTitle, "translationY", 0f, mBind?.tvTitle!!.height.toFloat()
        )
        val animSet = AnimatorSet()
        val duration = 40L
        alpha.duration = duration
        alpha2.duration = duration
        translationY.duration = duration
        translationY2.duration = duration
        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mBind?.tvTitle?.text = title
            }
        })
        animSet.duration = duration * 4
        animSet.play(alpha2).after(alpha)
        animSet.play(translationY).after(translationY2)
        animSet.start()
    }

    private fun search(searchText: String) {
        val fragment = fragmentPager!!.createFragment(mLastSelectedPosition)
        if (fragment is HomeFragment) {
            val searchData = fragment.search(searchText)
            if (null != searchData) {
                UiTools.circleReveal(mBind?.searchRv!!, true)
                val mSearchAdapter = HostAdapter(R.layout.layout_item_host)
                mBind?.searchRv!!.adapter = mSearchAdapter
                mSearchAdapter.setNewData(searchData)
            } else {
                UiTools.circleReveal(mBind?.searchRv!!, false)
            }
        }
    }

    override fun onDestroy() {
        unbindService(mConnection)
        super.onDestroy()
    }
}