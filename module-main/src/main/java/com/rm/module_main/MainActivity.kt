package com.rm.module_main

import com.rm.baselisten.activity.BaseActivity
import com.rm.component_comm.ARouterUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class MainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initView() {
        btnTest.setOnClickListener{
            ARouterUtils.getHomeService().login(this)
        }
    }

    override fun initData() {
    }
}