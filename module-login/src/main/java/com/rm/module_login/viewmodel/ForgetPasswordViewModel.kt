package com.rm.module_login.viewmodel

import android.app.Activity
import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.R
import com.rm.module_login.activity.VerificationInputActivity
import com.rm.module_login.repository.LoginRepository

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
class ForgetPasswordViewModel(private val repository: LoginRepository) : BaseVMViewModel() {
    var context: Activity? = null
    // 标识是否输入了文本
    var isInputText = ObservableField<Boolean>()
    //  输入的电话号码值
    var phone = ObservableField<String>()
    // 国家代码具体值
    var countryCode = ObservableField<String>()
    // 错误提示信息
    var errorTips = ObservableField<String>("")

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
        // toto 网络通过手机获取验证码
        if(phone.get()!!.length < 7){
            ToastUtil.show(context,context!!.resources.getString(R.string.login_input_right_number_tips))
            return
        }
        VerificationInputActivity.startActivity(context!!,phone.get().toString())
    }

    /**
     * 置空输入框的值
     */
    fun clearInput(){
        phone.set("")
    }

    /**
     * 显示国家列表选择框
     */
    fun showCountryList(){
        // todo
    }
}