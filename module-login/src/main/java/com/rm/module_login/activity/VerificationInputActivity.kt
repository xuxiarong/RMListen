package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityVerificationCodeInputBinding
import com.rm.module_login.viewmodel.VerificationInputViewModel

/**
 * desc   : 验证码输入界面
 * date   : 2020/08/27
 * version: 1.0
 */
class VerificationInputActivity :
    BaseVMActivity<LoginActivityVerificationCodeInputBinding, VerificationInputViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, VerificationInputActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_verification_code_input

    override fun startObserve() {
    }

    override fun initData() {
    }
}