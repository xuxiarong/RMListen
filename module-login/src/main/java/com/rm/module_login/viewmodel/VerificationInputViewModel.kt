package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.repository.LoginRepository

/**
 * desc   : 验证码输入ViewModel
 * date   : 2020/08/27
 * version: 1.0
 */
class VerificationInputViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    // 发送验证码的手机号码
    var phone = ObservableField<String>()

    // 输入的验证码
    var inputVerifyCode = ObservableField<String>("")

    var code = "0"

    // 监听绑定输入框内容变化
    var completeInput: (String) -> Unit = {
        completeInput(it)
    }

    private fun completeInput(content: String) {
        // 验证码输入完成
        inputVerifyCode.set(content)
        DLog.i("llj", "完成-----inputVerifyCode---->>>${inputVerifyCode.get()}")
    }
}