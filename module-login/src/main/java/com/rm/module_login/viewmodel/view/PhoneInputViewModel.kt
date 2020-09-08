package com.rm.module_login.viewmodel.view

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.util.DLog
import com.rm.module_login.bean.Country

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
     * 选择国家方法
     * @param country Country
     */
    fun choiceCountry(country: Country) {
        DLog.i("llj", "国家item点击------>>>${country.cn}")
        countryCode.set(country.phone_code)
    }
}