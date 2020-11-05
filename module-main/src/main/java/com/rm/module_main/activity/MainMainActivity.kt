package com.rm.module_main.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.adapter.MyViewPagerAdapter
import com.rm.module_main.customview.bottomtab.NavigationController
import com.rm.module_main.customview.bottomtab.item.NormalItemView
import com.rm.module_main.databinding.MainActivityMainBindingImpl
import com.rm.module_main.viewmodel.HomeMainViewModel
import kotlinx.android.synthetic.main.main_activity_main.*

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainMainActivity : BaseVMActivity<MainActivityMainBindingImpl,HomeMainViewModel>() {

    private lateinit var navigationController: NavigationController

    override fun getLayoutId() = R.layout.main_activity_main

    override fun initView() {

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

        view_pager.adapter = MyViewPagerAdapter(
                supportFragmentManager,
                navigationController.itemCount
            )

        view_pager.offscreenPageLimit = 5
//        navigationController.setMessageNumber(3, 8)
//        navigationController.setHasMessage(1, true)
        navigationController.setupWithViewPager(view_pager)

    }

    override fun onResume() {
        super.onResume()
        val playService = RouterHelper.createRouter(PlayService::class.java)
        rootViewAddView(playService.getGlobalPlay())
        playService.showView(this)
        if(view_pager.currentItem!= currentTab){
            view_pager.setCurrentItem(currentTab,false)
        }
    }

    override fun initData() {

    }

    companion object{

        var currentTab = 0;

        fun startMainActivity(context: Context,selectTab : Int = 0){
            //如果context 已经是MainMainActivity，而且则不需要跳转了
            if(context is MainMainActivity && selectTab == currentTab){
                return
            }
            currentTab = selectTab
            context.startActivity(Intent(context,MainMainActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }
}