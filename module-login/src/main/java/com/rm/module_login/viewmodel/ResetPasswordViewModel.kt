package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.view.PasswordInputViewModel

/**
 * desc   :
 * date   : 2020/08/28
 * version: 1.0
 */
class ResetPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    var verifyCode = ""
    var phone = ""

    // 密码输入框的ViewModel
    val passwordInputViewModel = PasswordInputViewModel()

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 修改密码
     */
    fun modify() {
        // 网络请求，根据验证码修改密码
        showLoading()
        launchOnIO {
            repository.resetPasswordByVerifyCode(
                phone,
                verifyCode,
                passwordInputViewModel.password.get()!!
            ).checkResult(
                onSuccess = {
                    showToast(R.string.login_modify_success)
                    showContentView()
                    finish()
                },
                onError = {
                    it?.let { showToast(it) }
                    showContentView()
                }
            )
        }
    }
}