package com.rm.module_login.viewmodel.view

import androidx.databinding.ObservableField

/**
 * desc   :
 * date   : 2020/08/31
 * version: 1.0
 */
class PhoneInputViewModel {
    //  输入的电话号码值
    var phone = ObservableField<String>("")

    // 国家代码具体值
    var countryCode = ObservableField<String>("+86")

    /**
     * 置空输入框的值
     */
    fun clearInput() {
        phone.set("")
    }
}