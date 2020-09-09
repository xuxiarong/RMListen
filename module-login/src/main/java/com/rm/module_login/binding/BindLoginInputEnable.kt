package com.rm.module_login.binding

import android.text.TextUtils
import android.view.View
import androidx.databinding.BindingAdapter

/**
 * desc   : 登陆电话输入框 或者 密码输入框规则验证扩展函数
 * date   : 2020/09/09
 * version: 1.0
 */
//@BindingAdapter("bindPoneStatusEnable")
@BindingAdapter("bindPhoneEnable", "bindPasswordEnable",requireAll = false)
fun View.bindLoginPhoneAndPasswordEnable(phone: String?, password: String?) {
    isEnabled = if (phone == null && password == null) {
        // 都为空
        false
    } else if (phone != null && password == null) {
        // 只是验证电话是否ok
        !TextUtils.isEmpty(phone)
    } else if (phone == null && password != null) {
        // 只是验证密码是否ok
        // 密码验证规则正则表达式
        val passwordRegex = Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,16}\$")
        passwordRegex.matches(password)
    } else {
        val passwordRegex = Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,16}\$")
        !TextUtils.isEmpty(phone) && passwordRegex.matches(password!!)
    }
}