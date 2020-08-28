package com.rm.module_play.activity

import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.business_lib.playview.GlobalplayHelp
import com.rm.module_play.R
import java.util.logging.Handler

class TestActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_test

    override fun initData() {

    }

    override fun initView() {
        if (GlobalplayHelp.instance.globalView.parent!=null) {
            (  GlobalplayHelp.instance.globalView.parent as FrameLayout).removeView(GlobalplayHelp.instance.globalView)
        }


    }

    override fun onResume() {
        super.onResume()
        getRootView().addView(GlobalplayHelp.instance.globalView, layoutParams)
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