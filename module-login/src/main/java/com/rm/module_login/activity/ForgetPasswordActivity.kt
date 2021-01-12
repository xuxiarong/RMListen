package com.rm.module_login.activity

import android.app.Activity
import android.content.Intent
import android.view.WindowManager
import com.rm.baselisten.helper.KeyboardStatusDetector.Companion.bindKeyboardVisibilityListener
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityForgetPasswordBinding
import com.rm.module_login.utils.CountryListDialogHelper
import com.rm.module_login.viewmodel.ForgetPasswordViewModel
import kotlinx.android.synthetic.main.login_activity_forget_password.*
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*

/**
 * desc   : 忘记密码界面
 * date   : 2020/08/27
 * version: 1.0
 */
class ForgetPasswordActivity :
    BaseVMActivity<LoginActivityForgetPasswordBinding, ForgetPasswordViewModel>() {

    companion object {
        fun startActivity(activity: Activity, phone: String, countryCode: String) {
            activity.startActivityForResult(
                Intent(
                    activity,
                    ForgetPasswordActivity::class.java
                ).apply {
                    putExtra("phone", phone)
                    putExtra("countryCode", countryCode)
                }, 200
            )
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

        //输入框默认获取焦点
        login_by_verify_code_input.requestFocus()
        //弹起输入法
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) // 设置默认键盘弹出

        //监听键盘的显示/隐藏
        bindKeyboardVisibilityListener { b, _ ->
            if (!b) {
                login_by_verify_code_input.clearFocus()
            }
        }
        //点击空白隐藏输入法
        login_forget_root_view.setOnClickListener {
            hideKeyboard(login_forget_root_view)
            login_by_verify_code_input.clearFocus()
        }
    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        login_by_verify_code_input.setSelection(login_by_verify_code_input.text.toString().length)
    }

    override fun finish() {
        intent.putExtra("inputPhone", mViewModel.phoneInputViewModel.phone.get()!!)
        intent.putExtra("inputCountryCode", mViewModel.phoneInputViewModel.countryCode.get()!!)
        setResult(0x02, intent)
        super.finish()
    }
}