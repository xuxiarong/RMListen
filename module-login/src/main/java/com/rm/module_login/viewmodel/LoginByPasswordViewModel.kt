package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.model.BaseStatusModel
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
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

    // 回调到Activity中的方法块
    var callBackToActivity: (Int) -> Unit = {}

    /**
     * 登陆
     */
    fun login() {
        // 登陆
        if (!isCheck.get()!!) {
            // 未选中check box
            callBackToActivity(0)
            return
        }

        baseStatusModel.value = BaseStatusModel(BaseNetStatus.BASE_SHOW_LOADING)
        launchOnIO {
            repository.loginByPassword(
                "${phoneInputViewModel.countryCode.get()}${phoneInputViewModel.phone.get()}",
                passwordInputViewModel.password.get()!!
            ).checkResult(
                onSuccess = {
                    baseStatusModel.value = BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT)
                    callBackToActivity(3)
                },
                onError = {
                    baseStatusModel.value = BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT)
                    errorTips.set(it)
                }
            )
        }

    }

    /**
     * 忘记密码
     */
    fun forgetPassword() {
        callBackToActivity(1)
    }

    /**
     * 验证码登陆
     */
    fun loginByVerifyCode() {
        callBackToActivity(2)
    }
}