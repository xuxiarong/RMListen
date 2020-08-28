package com.rm.module_main.activity

import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.R
import com.rm.module_main.adapter.MyViewPagerAdapter
import com.rm.module_main.customview.NoTouchViewPager
import com.rm.module_main.customview.bottomtab.BottomTabView
import com.rm.module_main.customview.bottomtab.NavigationController
import com.rm.module_main.customview.bottomtab.item.NormalItemView

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainMainActivity : BaseActivity() {

    private lateinit var navigationController: NavigationController

    override fun getLayoutId() = R.layout.main_activity_main
    lateinit var homeService: HomeService
    lateinit var mineService: MineService

    //    override fun initView() {
////        homeService = (navigateTo(ConstantsARouter.Home.PATH_HOME_SERVICE) as HomeService)
//        homeService = RouterHelper.createRouter(HomeService::class.java)
//
//        btnTest.setOnClickListener {
//            homeService.routerTest(this@MainActivity)
//        }
//
////        mineService = (navigateTo(ConstantsARouter.Mine.PATH_MINE_SERVICE) as MineService)
//        mineService = RouterHelper.createRouter(MineService::class.java)
//
//
//        btnLogin.setOnClickListener {
//            mineService.routerLogin(this)
//        }
//    }

    override fun initView() {
        navigationController = findViewById<BottomTabView>(R.id.mainTab).custom().run {

            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_listen_bar,
                    R.drawable.main_ic_home_tab_listen_bar_selected,
                    getString(R.string.main_tab_listen_bar)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_disable))
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_search,
                    R.drawable.main_ic_home_tab_search_selected,
                    getString(R.string.main_tab_search)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_disable))
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_my_listen,
                    R.drawable.main_ic_home_tab_my_listen_selected,
                    getString(R.string.main_tab_my_listen)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_disable))
            })
            addItem(NormalItemView(this@MainMainActivity).apply {
                initialize(
                    R.drawable.main_ic_home_tab_user,
                    R.drawable.main_ic_home_tab_user_selected,
                    getString(R.string.main_tab_user)
                )
                setTextCheckedColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_accent))
                setTextDefaultColor(ContextCompat.getColor(this@MainMainActivity, R.color.main_color_disable))
            })
        }.build()
        navigationController.addPlaceholder(2)
        var viewPager = findViewById<NoTouchViewPager>(R.id.view_pager).apply {
            adapter = MyViewPagerAdapter(
                supportFragmentManager,
                navigationController.itemCount
            )
        }
        navigationController.setMessageNumber(3,8)
        navigationController.setHasMessage(1,true)
        navigationController.setupWithViewPager(viewPager)

        val playService = RouterHelper.createRouter(PlayService::class.java)
        playService.addGlobalPlay(getRootView(),layoutParams)


    }

    override fun initData() {
    }
}