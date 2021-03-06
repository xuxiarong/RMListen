package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_FORGET_PWD
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_FORGET_PWD
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
            showTip(CONTEXT.getString(R.string.login_input_right_number_tips))
            return
        }

        showLoading()
        // 获取短信验证码
        launchOnIO {
            repository.sendMessage(
                CODE_TYPE_FORGET_PWD,
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!
            ).checkResult(
                onSuccess = {
                    showContentView()
                    showTip(CONTEXT.getString(R.string.login_send_success))

                    // 跳转到验证码输入界面
                    startActivity(
                        VerificationInputActivity::class.java,
                        VerificationInputActivity.getIntent(
                            phoneInputViewModel.countryCode.get()!!,
                            phoneInputViewModel.phone.get()!!,
                            TYPE_FORGET_PWD
                        )
                    )
                    finish()
                },
                onError = {it,_->
                    showContentView()
                    it?.let { showTip(it, R.color.business_color_ff5e5e) }
                }
            )
        }
    }
}