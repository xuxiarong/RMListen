package com.rm.module_play.activity

import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.R

class TestActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_test

    override fun initData() {

    }

    override fun initView() {
        rootViewAddView(GlobalplayHelp.instance.globalView)

    }

    override fun onResume() {
        super.onResume()
        GlobalplayHelp.instance.globalView.play(R.drawable.business_defualt_img)
        GlobalplayHelp.instance.globalView.show()
    }
    override fun onPause() {
        super.onPause()
        GlobalplayHelp.instance.globalView.hide()
    }
}