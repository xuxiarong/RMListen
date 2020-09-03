package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.model.BaseStatusModel
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
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

    // 回调到Activity中的方法块
    var callBackToActivity: (Int) -> Unit = {}

    var testDialogData = MutableLiveData<MutableList<LoginDialogModel>>(mutableListOf())

    /**
     * 获取验证码
     */
    fun getCode() {
        // 网络通过手机获取验证码
        if (!isCheck.get()!!) {
            // 未选中check box
            callBackToActivity(0)
            return
        }
        if (phoneInputViewModel.phone.get()!!.length < 7) {
            callBackToActivity(1)
            return
        }

        baseStatusModel.value = BaseStatusModel(BaseNetStatus.BASE_SHOW_LOADING)
        // 发送登陆短信验证码
        launchOnIO {
            repository.sendLoginVerifyCode(
                phoneInputViewModel.countryCode.get()!!,
                phoneInputViewModel.phone.get()!!
            ).checkResult(
                onSuccess = {
                    baseStatusModel.value = BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT)
                    callBackToActivity(2)
                },
                onError = {
                    baseStatusModel.value = BaseStatusModel(BaseNetStatus.BASE_SHOW_CONTENT)
                    errorTips.set(it)
                }
            )
        }


    }

    /**
     * 密码登陆入口
     */
    fun loginByPassword() {
        callBackToActivity(3)
    }

    fun testClick() {
        var currentStatus = loginStatus.get()!!
        currentStatus += 1
        loginStatus.set(currentStatus % 3)

        val newData = testDialogData.value
        newData?.add(LoginDialogModel("张三$currentStatus"))
        testDialogData.value = newData

       }

    fun getDialogData() {
        testDialogData.value =
            mutableListOf(
                LoginDialogModel("张三"),
                LoginDialogModel("李四"),
                LoginDialogModel("王五")
            )
    }

}