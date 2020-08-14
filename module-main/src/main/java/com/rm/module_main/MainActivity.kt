package com.rm.module_main

import com.rm.baselisten.activity.BaseActivity
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.navigateTo
import kotlinx.android.synthetic.main.activity_main.*

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_main

    lateinit var homeService:HomeService
    override fun initView() {
        homeService = (navigateTo(ConstantsARouter.Home.PATH_HOME_SERVICE) as HomeService)
        btnTest.setOnClickListener{
            homeService.login(this)
//            (navigateWithTo(ARouterPathConstance.PATH_HOME_SERVICE).navigation() as HomeService).login(this)
//            ARouterUtils.getHomeService().login(this)
        }
    }

    override fun initData() {
    }
}