package com.rm.module_play.activity

import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.R

class TestActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_test

    override fun initData() {

    }

    override fun initView() {
        GlobalplayHelp.instance.addGlobalPlayHelp(getRootView(),layoutParams)


    }

    override fun onResume() {
        super.onResume()

        GlobalplayHelp.instance.globalView.play(R.drawable.business_defualt_img)
        android.os.Handler().postDelayed({
            GlobalplayHelp.instance.globalView.show()
        },1000)
    }
    override fun onPause() {
        super.onPause()
        GlobalplayHelp.instance.globalView.hide()
    }
}