package com.rm.module_home

import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.baselisten.activity.BaseActivity

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = "/home/TestActivity")
class TestActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_test

    override fun initView() {
    }

    override fun initData() {
    }
}