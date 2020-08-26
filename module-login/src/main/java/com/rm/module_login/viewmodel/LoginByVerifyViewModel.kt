package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.module_login.repository.LoginRepository

/**
 * desc   : 登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyViewModel(private val repository: LoginRepository) : BaseNetViewModel() {
    // 输入框取消按钮是否显示标识
    var isCancelBtnVisible = MutableLiveData<Boolean>(false)
    var phone = ObservableField<String>("")

    // 国家代码具体值
    var countryCode = MutableLiveData<String>()

    val phoneNumber : (String ) -> Unit = {afterInputChange()}

    fun afterInputChange() {
        DLog.d("zean","phone = ${phone.get()}")
    }
}