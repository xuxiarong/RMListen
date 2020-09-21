package com.rm.module_login.viewmodel.view

import androidx.databinding.ObservableField
import com.rm.module_login.utils.CountryDataManager

/**
 * desc   :
 * date   : 2020/08/31
 * version: 1.0
 */
class PhoneInputViewModel {
    //  输入的电话号码值
    var phone = ObservableField<String>("")

    // 国家代码具体值
    var countryCode = ObservableField<String>("+${CountryDataManager.choiceCountry.phone_code}")

    /**
     * 置空输入框的值
     */
    fun clearInput() {
        phone.set("")
    }
}