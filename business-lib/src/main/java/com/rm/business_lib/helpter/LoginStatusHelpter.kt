package com.rm.business_lib.helpter

import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.*
import com.rm.business_lib.bean.LoginUserBean

/**
 * desc   : 登陆成功的帮助工具类
 * date   : 2020/09/09
 * version: 1.0
 */
/**
 * 登入
 * @param access String 访问token
 * @param refresh String 刷新token
 * @param userInfo LoginUserBean 登陆的用户信息
 */
fun loginIn(access:String,refresh:String,userInfo: LoginUserBean) {
    // 保存登陆信息到本地
    ACCESS_TOKEN.putMMKV("Bearer $access")
    REFRESH_TOKEN.putMMKV("Bearer $refresh")
    LOGIN_USER_INFO.putMMKV(loginUser)

    // 改变当前是否用户登陆状态 和 登陆的用户信息
    isLogin.postValue(true)
    loginUser.postValue(userInfo)
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
    isLogin.postValue(false)
    loginUser.postValue(null)
}