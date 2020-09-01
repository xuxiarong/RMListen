package com.rm.module_login.bean

import android.text.TextUtils

/**
 * desc   : 登录信息
 * date   : 2020/09/01
 * version: 1.0
 */
data class LoginInfo(val access: String, val refresh: String) {
    /**
     * 判断当前是否登陆
     * @return Boolean
     */
    fun isLogin(): Boolean {
        return TextUtils.isEmpty(access) && TextUtils.isEmpty(refresh)
    }
}