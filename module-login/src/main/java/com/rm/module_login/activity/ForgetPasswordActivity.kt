package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityForgetPasswordBinding
import com.rm.module_login.utils.CountryListDialogHelper
import com.rm.module_login.viewmodel.ForgetPasswordViewModel
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*

/**
 * desc   : 忘记密码界面
 * date   : 2020/08/27
 * version: 1.0
 */
class ForgetPasswordActivity :
    BaseVMActivity<LoginActivityForgetPasswordBinding, ForgetPasswordViewModel>() {

    companion object {
        fun startActivity(context: Context, phone: String) {
            context.startActivity(Intent(context, ForgetPasswordActivity::class.java).apply {
                putExtra("phone", phone)
            })
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.login_activity_forget_password

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        mViewModel.baseTitleModel.value = BaseTitleModel().setLeftIconClick { finish() }
        mViewModel.phoneInputViewModel.phone.set(intent.getStringExtra("phone"))
        login_include_phone_input_country_code.setOnClickListener {
            CountryListDialogHelper.show(this, mViewModel, mViewModel.phoneInputViewModel)
        }

        login_include_phone_input_arrow_view.setOnClickListener {
            CountryListDialogHelper.show(this, mViewModel, mViewModel.phoneInputViewModel)
        }
    }

    override fun initData() {
    }
}