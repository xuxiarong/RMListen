package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityLoginByVerifyCodeBinding
import com.rm.module_login.viewmodel.LoginByVerifyViewModel

/**
 * desc   : 验证码登陆界面
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyCodeActivity :
    BaseVMActivity<LoginActivityLoginByVerifyCodeBinding, LoginByVerifyViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginByVerifyCodeActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_login_by_verify_code

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        mViewModel.phone.set("123")
        dataBind.run {
            viewModel = mViewModel
        }
    }

    override fun initData() {
        mViewModel.countryCode.value = "+833"
        mViewModel.isCancelBtnVisible.value = true
    }
}