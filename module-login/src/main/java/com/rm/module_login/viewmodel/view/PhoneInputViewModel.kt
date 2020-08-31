package com.rm.module_login.viewmodel.view

import android.text.TextUtils
import androidx.databinding.ObservableField

/**
 * desc   :
 * date   : 2020/08/31
 * version: 1.0
 */
class PhoneInputViewModel {
    // 标识是否输入了文本
    var isInputText = ObservableField<Boolean>(false)
    //  输入的电话号码值
    var phone = ObservableField<String>("")
    // 国家代码具体值
    var countryCode = ObservableField<String>("+86")

    // 监听绑定输入框内容变化
    val checkInput: (String) -> Unit = { afterInputChange() }

    private fun afterInputChange() {
        // 设置是否输入了文本
        isInputText.set(!TextUtils.isEmpty(phone.get()))
    }

    /**
     * 置空输入框的值
     */
    fun clearInput() {
        phone.set("")
    }

    /**
     * 显示国家列表选择框
     */
    fun showCountryList() {
        // todo
    }
}