package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityResetPasswordBinding
import com.rm.module_login.viewmodel.ResetPasswordViewModel

/**
 * desc   :
 * date   : 2020/08/28
 * version: 1.0
 */
class ResetPasswordActivity :
    BaseVMActivity<LoginActivityResetPasswordBinding, ResetPasswordViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ResetPasswordActivity::class.java))
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.login_activity_reset_password

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        mViewModel.baseTitleModel.value = BaseTitleModel().setLeftIconClick { finish() }
    }


    override fun initData() {
    }
}