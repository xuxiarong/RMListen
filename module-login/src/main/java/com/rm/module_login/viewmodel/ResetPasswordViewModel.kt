package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_CHANGE_PWD
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_FORGET_PWD
import com.rm.module_login.viewmodel.view.PasswordInputViewModel

/**
 * desc   :
 * date   : 2020/08/28
 * version: 1.0
 */
class ResetPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    var verifyCode = ""
    var countryCode = ""
    var phone = ""
    var type = ""

    // 密码输入框的ViewModel
    val passwordInputViewModel = PasswordInputViewModel()

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 修改密码
     */
    fun modify() {
        // 网络请求，根据验证码修改密码
        when (type) {
            CODE_TYPE_FORGET_PWD -> {
                forgetPassword()
            }
            CODE_TYPE_CHANGE_PWD -> {
                changePassword()
            }
        }
    }

    /**
     * 修改密码
     */
    private fun changePassword() {
        showLoading()
        launchOnIO {
            repository.changePassword(
                verifyCode,
                passwordInputViewModel.password.get()!!
            ).checkResult(
                onSuccess = {
                    showToast(R.string.login_modify_success)
                    showContentView()
                    finish()
                },
                onError = { it, _ ->
                    it?.let { showToast(it) }
                    showContentView()
                }
            )
        }
    }

    /**
     * 忘记密码
     */
    private fun forgetPassword() {
        showLoading()
        launchOnIO {
            repository.forgetPassword(
                countryCode,
                phone,
                verifyCode,
                passwordInputViewModel.password.get()!!
            ).checkResult(
                onSuccess = {
                    showToast(R.string.login_modify_success)
                    showContentView()
                    finish()
                },
                onError = { it, _ ->
                    it?.let { showToast(it) }
                    showContentView()
                }
            )
        }
    }
}