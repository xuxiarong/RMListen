package com.rm.module_login.model

import androidx.databinding.ObservableField
import com.rm.baselisten.util.DLog

/**
 * desc   :
 * date   : 2020/08/31
 * version: 1.0
 */
data class PhoneInputViewModel(
    var isInputText: Boolean = false,
    var phone: ObservableField<String>,
    var countryCode: String = "",
    val showCountryList: () -> Unit
) {

    init {
        phone.set("")
    }

    fun clearPhoneInput(){
        phone.set("")
    }

    fun testShow(){
        DLog.d("suolong","test ")
    }
}