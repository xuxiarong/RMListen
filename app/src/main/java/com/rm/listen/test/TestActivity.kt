package com.rm.listen.test

import androidx.lifecycle.Observer
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.component_comm.*
import com.rm.component_comm.interceptor.LoginNavigationCallbackImpl
import com.rm.listen.databinding.ActivityTestBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestActivity : BaseNetActivity<ActivityTestBinding, TestViewModel>() {

    private val testViewModel by viewModel<TestViewModel>()
    override fun getViewModel(): TestViewModel = testViewModel

    override fun initView() {
        databind.run {
            viewModel = testViewModel
        }
        testViewModel.downTimeData.observe(this, Observer {
            databind.downTime.text = it

        })

    }

    override fun initData() {
        testViewModel.zipLoad()
        databind.toMine.setOnClickListener {
            navigateToForResult(ConstantsARouter.Mine.F_MAIN, 1)
        }

        databind.hasLogin.setOnClickListener {
            navigateWithTo(ConstantsARouter.Mine.F_MAIN).withBoolean(
                IS_LOGIN,
                IS_LOGIN.getBooleanMMKV(false)
            ).navigation(this, LoginNavigationCallbackImpl())
        }
        databind.login.setOnClickListener {
            navigateWithTo(ConstantsARouter.Mine.F_TEST).withBoolean(
                IS_LOGIN,
                IS_LOGIN.getBooleanMMKV(false)
            ).navigation(this, LoginNavigationCallbackImpl())

        }
        databind.needLogin.setOnClickListener {
            navigateWithTo(ConstantsARouter.Mine.F_TEST).withBoolean(
                IS_LOGIN,
                IS_LOGIN.getBooleanMMKV(false)
            ).navigation(this, LoginNavigationCallbackImpl())
        }


    }

    override fun startObserve() {
    }
}