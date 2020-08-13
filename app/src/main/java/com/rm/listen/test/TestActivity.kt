package com.rm.listen.test

import android.os.Bundle
import androidx.lifecycle.Observer
import com.rm.baselisten.activity.BaseNetActivity
import com.rm.listen.R
import com.rm.listen.bean.Title
import com.rm.listen.databinding.ActivityLoginBinding
import com.rm.listen.databinding.ActivityTestBinding
import com.rm.listen.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestActivity : BaseNetActivity<ActivityTestBinding, TestViewModel>()  {

    private val testViewModel by viewModel<TestViewModel>()
    override fun getViewModel(): TestViewModel =testViewModel

    override fun initView() {
        databind.run {
            viewModel = testViewModel
        }
        testViewModel.downTimeData.observe(this, Observer {
            databind.downTime.text=it

        })

    }

    override fun initData() {
        testViewModel.zipLoad()
    }

    override fun startObserve() {
    }

}