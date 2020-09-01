package com.rm.module_login.viewmodel

import android.app.Activity
import androidx.databinding.ObservableField
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.ForgetPasswordActivity
import com.rm.module_login.activity.LoginByVerifyCodeActivity
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.view.PasswordInputViewModel
import com.rm.module_login.viewmodel.view.PhoneInputViewModel

/**
 * desc   : 登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {

    // 手机输入框的ViewModel
    val phoneInputViewModel = PhoneInputViewModel()

    // 密码输入框的ViewModel
    val passwordInputViewModel = PasswordInputViewModel()

    var context: Activity? = null

    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 登陆
     */
    fun login() {
        // todo 登陆
        if (!isCheck.get()!!) {
            // 未选中check box
            ToastUtil.show(context, context!!.resources.getString(R.string.login_agree_deal_tips))
            return
        }

    }

    /**
     * 忘记密码
     */
    fun forgetPassword() {
        ForgetPasswordActivity.startActivity(context!!)
    }

    /**
     * 验证码登陆
     */
    fun loginByVerifyCode() {
        LoginByVerifyCodeActivity.startActivity(context!!)
        context!!.finish()
    }
}