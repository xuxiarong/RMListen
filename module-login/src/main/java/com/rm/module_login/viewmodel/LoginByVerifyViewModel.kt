package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.LoginByPasswordActivity
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.activity.VerificationInputActivity.Companion.TYPE_LOGIN
import com.rm.module_login.bean.LoginDialogModel
import com.rm.module_login.repository.LoginRepository
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

//    // 回调到Activity中的方法块
//    var callBackToActivity: (Int) -> Unit = {}

    var testDialogData = MutableLiveData<MutableList<LoginDialogModel>>(mutableListOf())

    /**
     * 获取验证码
     */
    fun getCode() {

        showToast(R.string.login_agree_deal_tips)

        // 网络通过手机获取验证码
        if (!isCheck.get()!!) {
            // 未选中check box
            showToast(R.string.login_agree_deal_tips)
            return
        }
        if (phoneInputViewModel.phone.get()!!.length < 7) {
            showToast(R.string.login_input_right_number_tips)
            return
        }

        showLoading()
        // 发送登陆短信验证码
        launchOnIO {
            repository.sendLoginVerifyCode(
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!
            ).checkResult(
                onSuccess = {
                    showContentView()
                    showToast(R.string.login_send_success)
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
                onError = {
                    showContentView()
                    errorTips.set(it)
                }
            )
        }
    }

    /**
     * 密码登陆入口
     */
    fun loginByPassword() {
        // 密码登陆
        startActivity(LoginByPasswordActivity::class.java)
        finish()
    }

    fun testClick() {
        var currentStatus = loginStatus.get()!!
        currentStatus += 1
        loginStatus.set(currentStatus % 3)

        val newData = testDialogData.value
        newData?.add(LoginDialogModel("张三$currentStatus"))
        testDialogData.value = newData

    }

//    fun getDialogData() {
//        testDialogData.value =
//            mutableListOf(
//                LoginDialogModel("张三"),
//                LoginDialogModel("李四"),
//                LoginDialogModel("王五")
//            )
//    }

}