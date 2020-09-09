package com.rm.module_login.utils

import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.ACCESS_TOKEN
import com.rm.business_lib.LOGIN_USER_INFO
import com.rm.business_lib.REFRESH_TOKEN
import com.rm.module_login.LoginConstants
import com.rm.module_login.bean.LoginInfo

/**
 * desc   : 登陆成功的帮助工具类
 * date   : 2020/09/09
 * version: 1.0
 */
/**
 * 登入
 * @param loginInfo LoginInfo
 */
fun loginIn(loginInfo: LoginInfo) {
    loginInfo.let {
        // 保存登陆信息到本地
        ACCESS_TOKEN.putMMKV(it.access)
        REFRESH_TOKEN.putMMKV(it.refresh)
        LOGIN_USER_INFO.putMMKV(it.member)

        // 改变当前是否用户登陆状态 和 登陆的用户信息
        LoginConstants.isLogin.value = true
        LoginConstants.loginUser.value = it.member
    }
}

/**
 * 登出
 */
fun loginOut() {
    // 保存登陆信息到本地
    ACCESS_TOKEN.putMMKV("")
    REFRESH_TOKEN.putMMKV("")
    LOGIN_USER_INFO.putMMKV("")

    // 改变当前是否用户登陆状态 和 登陆的用户信息
    LoginConstants.isLogin.value = false
    LoginConstants.loginUser.value = null
}