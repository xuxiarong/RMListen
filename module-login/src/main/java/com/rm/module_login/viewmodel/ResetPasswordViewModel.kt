package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.view.PasswordInputViewModel

/**
 * desc   :
 * date   : 2020/08/28
 * version: 1.0
 */
class ResetPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    // 密码输入框的ViewModel
    val passwordInputViewModel = PasswordInputViewModel()

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    /**
     * 修改密码
     */
    fun modify() {
        // todo 网络请求，修改密码
    }
}