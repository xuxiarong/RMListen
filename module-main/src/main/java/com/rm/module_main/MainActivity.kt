package com.rm.module_main

import com.rm.baselisten.activity.BaseActivity
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.navigateTo
import kotlinx.android.synthetic.main.activity_main.*

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_main

    lateinit var homeService: HomeService
    lateinit var mineService: MineService
    override fun initView() {
        homeService = (navigateTo(ConstantsARouter.Home.PATH_HOME_SERVICE) as HomeService)

        btnTest.setOnClickListener {
            homeService.routerTest(this@MainActivity)
        }

        mineService = (navigateTo(ConstantsARouter.Mine.PATH_MINE_SERVICE) as MineService)


        btnLogin.setOnClickListener {
            mineService.routerLogin(this)
        }
    }

    override fun initData() {
    }
}