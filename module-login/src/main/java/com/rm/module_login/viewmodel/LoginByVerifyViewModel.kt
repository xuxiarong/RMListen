package com.rm.module_login.viewmodel

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.module_login.repository.LoginRepository

/**
 * desc   : 登陆viewModel
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyViewModel(private val repository: LoginRepository) : BaseNetViewModel() {
    // 标识是否输入了文本
    var isInputText = ObservableField<Boolean>()
    //  输入的电话号码值
    var phone = ObservableField<String>()
    // 国家代码具体值
    var countryCode = ObservableField<String>()
    // 用户协议和隐私协议是否选择
    var isCheck = ObservableField<Boolean>(false)

    // 监听绑定输入框内容变化
    val checkInput: (String) -> Unit = { afterInputChange() }

    private fun afterInputChange() {
        // 设置是否输入了文本
        isInputText.set(!TextUtils.isEmpty(phone.get()))
    }

    /**
     * 获取验证码
     */
    fun getCode(){
        // TODO 网络通过手机获取验证码
        if(!isCheck.get()!!){
            // 未选中check box
        }
        DLog.i("llj","获取验证码！！！isCheck---->>${isCheck.get()}")
    }

    // 置空输入框的值
    fun clearInput(){
        phone.set("")
    }
}