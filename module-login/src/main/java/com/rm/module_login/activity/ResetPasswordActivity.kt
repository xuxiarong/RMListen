package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityResetPasswordBinding
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.ResetPasswordViewModel
import kotlinx.android.synthetic.main.login_activity_reset_password.*

/**
 * desc   :
 * date   : 2020/08/28
 * version: 1.0
 */
class ResetPasswordActivity :
    BaseVMActivity<LoginActivityResetPasswordBinding, ResetPasswordViewModel>() {
    companion object {
        fun startActivity(
            context: Context,
            verifyCode: String,
            countryCode: String,
            phone: String
        ) {
            context.startActivity(Intent(context, ResetPasswordActivity::class.java).apply {
                putExtra("verifyCode", verifyCode)
                putExtra("countryCode", countryCode)
                putExtra("phone", phone)
            })
        }

        fun getIntent(
            verifyCode: String,
            countryCode: String,
            phone: String,
            type: String
        ): HashMap<String, Any> {
            return hashMapOf(
                Pair("verifyCode", verifyCode),
                Pair("countryCode", countryCode),
                Pair("phone", phone),
                Pair("type", type)
            )
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.login_activity_reset_password

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        mViewModel.baseTitleModel.value = BaseTitleModel().setLeftIconClick { finish() }
        mViewModel.verifyCode = intent.getStringExtra("verifyCode") ?: ""
        mViewModel.phone = intent.getStringExtra("phone") ?: ""
        mViewModel.countryCode = intent.getStringExtra("countryCode") ?: ""
        mViewModel.type = intent.getStringExtra("type") ?: ""
    }


    override fun initData() {
    }
}