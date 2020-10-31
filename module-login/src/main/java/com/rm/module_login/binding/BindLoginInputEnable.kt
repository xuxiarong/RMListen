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
@BindingAdapter("bindCountryCode", "bindPhoneEnable", "bindPasswordEnable", requireAll = false)
fun View.bindLoginPhoneAndPasswordEnable(countryCode: String?, phone: String?, password: String?) {
    isEnabled = if (phone == null && password == null) {
        // 都为空
        false
    } else if (phone != null && password == null) {
        // 只是验证电话是否ok
        if (countryCode != null && TextUtils.equals(countryCode, "+86")) {
            // 如果是中国区的电话号码，则必须11位数 且 必须是1开头的
            phone.length == 11 && phone.startsWith("1")
        } else {
            !TextUtils.isEmpty(phone)
        }
    } else if (phone == null && password != null) {
        // 只是验证密码是否ok
        // 密码验证规则正则表达式
        // 密码的正则表达式
//    private val passwordRegex = Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,16}\$")
        // 密码的正则表达式(至少包含字母/数字/特殊字符 其中两种类型组合)
        val passwordRegex =
            Regex("(?!^\\d+\$)(?!^[A-Za-z]+\$)(?!^[^A-Za-z0-9]+\$)(?!^.*[\\u4E00-\\u9FA5].*\$)^\\S{8,16}\$")
        passwordRegex.matches(password)
    } else {
//        val passwordRegex = Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{8,16}\$")
        // 密码的正则表达式(至少包含字母/数字/特殊字符 其中两种类型组合)
        val passwordRegex =
            Regex("(?!^\\d+\$)(?!^[A-Za-z]+\$)(?!^[^A-Za-z0-9]+\$)(?!^.*[\\u4E00-\\u9FA5].*\$)^\\S{8,16}\$")
        if (phone != null && countryCode != null && TextUtils.equals(countryCode, "+86")) {
            // 如果是中国区的电话号码，则必须11位数
            phone.length == 11 && phone.startsWith("1") && passwordRegex.matches(password!!)
        } else {
            !TextUtils.isEmpty(phone) && passwordRegex.matches(password!!)
        }
    }
}