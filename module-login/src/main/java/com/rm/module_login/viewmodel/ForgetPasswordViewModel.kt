package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_RESET_PWD
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.view.PhoneInputViewModel

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class ForgetPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {

    // 手机输入框的ViewModel
    val phoneInputViewModel = PhoneInputViewModel()

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 获取验证码
     */
    fun getCode() {
        // toto 网络通过手机获取验证码
        if (phoneInputViewModel.phone.get()!!.length < 7) {
            showToast(R.string.login_input_right_number_tips)
            return
        }

        showLoading()
        // 获取短信验证码
        launchOnIO {
            repository.sendForgetPasswordVerifyCode(
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!
            ).checkResult(
                onSuccess = {
                    showContentView()
                    showToast(R.string.login_send_success)

                    // 跳转到验证码输入界面
                    startActivity(
                        VerificationInputActivity::class.java,
                        VerificationInputActivity.getIntent(
                            phoneInputViewModel.countryCode.get()!!,
                            phoneInputViewModel.phone.get()!!,
                            TYPE_RESET_PWD
                        )
                    )
                },
                onError = {
                    showContentView()
                    it?.let { showToast(it) }
                }
            )
        }
    }
}