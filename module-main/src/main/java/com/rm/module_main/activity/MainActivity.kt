package com.rm.module_main.activity

import com.rm.baselisten.activity.BaseActivity
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.R
import com.rm.module_main.adapter.MyViewPagerAdapter
import com.rm.module_main.customview.NoTouchViewPager
import com.rm.module_main.customview.bottomtab.BottomTabView
import com.rm.module_main.customview.bottomtab.NavigationController
import com.rm.module_main.customview.bottomtab.item.NormalItemView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainActivity : BaseActivity() {

    private lateinit var navigationController: NavigationController

    override fun getLayoutResId(): Int = R.layout.activity_main
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
        navigationController = findViewById<BottomTabView>(R.id.tab).custom().run {

            addItem(NormalItemView(this@MainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
            addItem(NormalItemView(this@MainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
            addItem(NormalItemView(this@MainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
            addItem(NormalItemView(this@MainActivity).apply {
                initialize(R.drawable.ic_favorite_gray_24dp, R.drawable.ic_favorite_teal_24dp)
            })
        }.build()
        navigationController.addPlaceholder(2)
        var viewPager = findViewById<NoTouchViewPager>(R.id.view_pager).apply {
            adapter = MyViewPagerAdapter(
                supportFragmentManager,
                navigationController.itemCount
            )
        }

        navigationController.setupWithViewPager(viewPager)
    }

    override fun initData() {
    }
}