package com.rm.module_play.test

import android.content.Context
import android.content.Intent
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_play.BR
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.R
import com.rm.module_play.activity.PlayActivity
import com.rm.module_play.databinding.ActivityTestBinding

class TestActivity : BaseVMActivity<ActivityTestBinding, TestViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, TestActivity::class.java))
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_test


    override fun initView() {
        rootViewAddView(GlobalplayHelp.instance.globalView)

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        GlobalplayHelp.instance.globalView.show()
    }

    override fun onPause() {
        super.onPause()
        GlobalplayHelp.instance.globalView.hide()
    }

    override fun initModelBrId(): Int = BR.viewModel


    override fun startObserve() {
    }
}