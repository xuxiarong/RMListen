package com.rm.module_main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.HomeGlobalData
import com.rm.business_lib.PlaySettingData
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.adapter.MyViewPagerAdapter
import com.rm.module_main.customview.bottomtab.NavigationController
import com.rm.module_main.customview.bottomtab.item.NormalItemView
import com.rm.module_main.databinding.MainActivityMainBindingImpl
import com.rm.module_main.viewmodel.HomeMainViewModel
import com.tencent.mars.xlog.Log
import kotlinx.android.synthetic.main.main_activity_main.*

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainMainActivity :
    ComponentShowPlayActivity<MainActivityMainBindingImpl, HomeMainViewModel>() {

    private var changeCallBack: ViewPager2.OnPageChangeCallback? = null
    private lateinit var navigationController: NavigationController
    private var lastExitTime = 0L
    private var mAdapter: MyViewPagerAdapter? = null

    override fun getLayoutId() = R.layout.main_activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!TextUtils.isEmpty(jumpUrl)) {
            BannerJumpUtils.onBannerClick(this, jumpUrl)
        }
    }

    override fun initView() {
        mDataShowView = view_pager
        if (TextUtils.isEmpty(jumpUrl)) {
                initPager()
        } else {
            view_pager.postDelayed({
                initPager()
            }, 800)
        }
    }

    private fun initPager() {
        navigationController = mainTab.custom().run {
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_listen_bar,
                    R.drawable.main_ic_home_tab_listen_bar_selected,
                    getString(R.string.main_tab_listen_bar)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_search,
                    R.drawable.main_ic_home_tab_search_selected,
                    getString(R.string.main_tab_search)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_my_listen,
                    R.drawable.main_ic_home_tab_my_listen_selected,
                    getString(R.string.main_tab_my_listen)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_user,
                    R.drawable.main_ic_home_tab_user_selected,
                    getString(R.string.main_tab_user)
                )
                setTextCheckedColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_accent
                    )
                )
                setTextDefaultColor(
                    ContextCompat.getColor(
                        this@MainMainActivity,
                        R.color.main_color_disable
                    )
                )
            })
        }.build()
        navigationController.addPlaceholder(2)
        mAdapter = MyViewPagerAdapter(this, navigationController.itemCount)
        view_pager.adapter = mAdapter
        view_pager.isUserInputEnabled = false
        navigationController.setupWithViewPager(view_pager)
        changeCallBack = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentTab = position
                HomeGlobalData.homeGlobalSelectTab.set(position)
            }
        }
        changeCallBack?.let {
            view_pager.registerOnPageChangeCallback(it)
        }
        if (Intent.ACTION_VIEW == intent.action && intent.data != null) {
            val data = intent.data
            BannerJumpUtils.onBannerClick(context = this, url = data.toString())
            DLog.i("------>", "data:${data.toString()}")
        }
    }


    override fun onResume() {
        super.onResume()
        if (view_pager.currentItem != currentTab) {
            view_pager.setCurrentItem(currentTab, false)
        }
    }

    override fun onStart() {
        super.onStart()
        BusinessInsertManager.doInsertKey(BusinessInsertConstance.INSERT_TYPE_START)
    }

    override fun initData() {
        if (PlaySettingData.getContinueLastPlay()) {
            RouterHelper.createRouter(PlayService::class.java).requestAudioFocus()
            view_pager.postDelayed({
                BaseConstance.basePlayInfoModel.get()?.let {
                    mViewModel.getChapterListWithId(it.playAudioId, it.playChapterId)
                }
            }, 1500)
        }
    }

    override fun onBackPressed() {
        if (lastExitTime == 0L) {
            ToastUtil.show( "2???????????????????????????????????????App")
            lastExitTime = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - lastExitTime <= 2000) {
                Log.appenderClose()
                finish()
            } else {
                lastExitTime = System.currentTimeMillis()
                ToastUtil.show( "2???????????????????????????????????????App")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mAdapter?.getFragments()?.forEach {
            if (!it.isDetached) {
                it.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        changeCallBack?.let {
            view_pager.unregisterOnPageChangeCallback(it)
        }
        mAdapter = null
    }

    companion object {

        var currentTab = 0
        var jumpUrl = ""
        fun startMainActivity(context: Context, selectTab: Int = 0, splashUrl: String = "") {
            //??????context ?????????MainMainActivity??????????????????tab????????????tab?????????????????????
            if (context is MainMainActivity && selectTab == currentTab) {
                return
            }
            currentTab = selectTab
            jumpUrl = splashUrl
            context.startActivity(Intent(context, MainMainActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun isShowPlayWhenEmptyUrl() = true

    override fun startObserve() {

    }
}