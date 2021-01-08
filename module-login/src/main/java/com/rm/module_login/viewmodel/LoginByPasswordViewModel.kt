package com.rm.module_login.viewmodel

import android.content.Context
import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.helpter.loginIn
import com.rm.module_login.R
import com.rm.module_login.activity.ForgetPasswordActivity
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

    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 登陆
     */
    fun login() {
        // 登陆
        val countryCode = phoneInputViewModel.countryCode.get()!!
        val phone = phoneInputViewModel.phone.get()!!
        val password = passwordInputViewModel.password.get()!!
        when {
            (!isCheck.get()!!) -> {
                // 未选中check box
                showToast(R.string.login_agree_deal_tips)
                return
            }
            TextUtils.equals(countryCode, "+86")
                    && (phone.length < 11 || !phone.startsWith("1")) -> {
                errorTips.set("手机格式不对")
            }
            else -> {
                showLoading()
                launchOnIO {
                    repository.loginByPassword(
                        countryCode,
                        phone,
                        password
                    ).checkResult(
                        onSuccess = {
                            // 设置登陆成功数据
                            loginIn(it.access, it.refresh, it.member)

                            showContentView()
                            showToast(R.string.login_success)
                            finish()
                        },
                        onError = { it, _ ->
                            showContentView()
                            errorTips.set(it)
                        }
                    )
                }
            }
        }
    }

    /**
     * 忘记密码
     */
    fun forgetPassword(context: Context) {
        getActivity(context)?.let {
            ForgetPasswordActivity.startActivity(
                it,
                phoneInputViewModel.phone.get()!!,
                phoneInputViewModel.countryCode.get()!!
            )
        }
    }

    /**
     * 验证码登陆
     */
    fun loginByVerifyCode() {
        // 密码登陆界面一定是验证码登陆跳转过来的，所以这里直接finish就好
//        startActivity(LoginByVerifyCodeActivity::class.java)
        finish()
    }
}