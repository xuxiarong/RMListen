package com.rm.module_login.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.LoginByPasswordActivity
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_LOGIN
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.repository.LoginRepository.Companion.CODE_TYPE_LOGIN
import com.rm.module_login.viewmodel.view.PhoneInputViewModel

/**
 * desc   : 登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    var phoneInputViewModel = PhoneInputViewModel()

    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    var loginStatus = ObservableField(0)

    /**
     * 获取验证码
     */
    fun getCode() {

        // 网络通过手机获取验证码
        if (!isCheck.get()!!) {
            // 未选中check box
            showTip(
                CONTEXT.getString(R.string.login_agree_deal_tips),
                R.color.business_color_ff5e5e
            )
            return
        }
        if (phoneInputViewModel.phone.get()!!.length < 7) {
            showTip(
                CONTEXT.getString(R.string.login_input_right_number_tips),
                R.color.business_color_ff5e5e
            )
            return
        }

        showLoading()
        // 发送登陆短信验证码
        launchOnIO {
            repository.sendMessage(
                CODE_TYPE_LOGIN,
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!
            ).checkResult(
                onSuccess = {
                    showContentView()
                    showTip(CONTEXT.getString(R.string.login_send_success))
                    // 跳转到登陆验证码输入界面
                    startActivity(
                        VerificationInputActivity::class.java,
                        VerificationInputActivity.getIntent(
                            phoneInputViewModel.countryCode.get()!!,
                            phoneInputViewModel.phone.get()!!,
                            TYPE_LOGIN
                        )
                    )
                },
                onError = { it, _ ->
                    showContentView()
                    errorTips.set(it)
                }
            )
        }
    }

    /**
     * 密码登陆入口
     */
    fun loginByPassword(context: Context) {
        // 密码登陆
        getActivity(context)?.let {
            LoginByPasswordActivity.startActivity(
                it,
                phoneInputViewModel.phone.get()!!,
                phoneInputViewModel.countryCode.get()!!
            )
        }

    }

}