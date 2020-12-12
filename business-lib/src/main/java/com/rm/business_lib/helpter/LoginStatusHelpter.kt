package com.rm.business_lib.helpter

import android.text.TextUtils
import android.util.Base64
import com.google.gson.Gson
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.*
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.bean.TokenBean
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager

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
fun loginIn(access: String, refresh: String, userInfo: LoginUserBean) {
    // 保存登陆信息到本地
    ACCESS_TOKEN.putMMKV(access)
    REFRESH_TOKEN.putMMKV(refresh)
    LOGIN_USER_INFO.putMMKV(userInfo)
    // 解析访问令牌的过期时间，并保存解析出来的token失效的时间戳
    ACCESS_TOKEN_INVALID_TIMESTAMP.putMMKV(parseToken(access))

    // 改变当前是否用户登陆状态 和 登陆的用户信息
    isLogin.set(true)
    loginUser.set(userInfo)

    BusinessInsertManager.doInsertKey(BusinessInsertConstance.INSERT_TYPE_ADD)

}

/**
 * 登出
 */
fun loginOut() {
    // 保存登陆信息到本地
    DLog.e("llj","开始登出操作！！！")
    ACCESS_TOKEN.putMMKV("")
    REFRESH_TOKEN.putMMKV("")
    LOGIN_USER_INFO.putMMKV("")
    ACCESS_TOKEN_INVALID_TIMESTAMP.putMMKV(0)

    // 改变当前是否用户登陆状态 和 登陆的用户信息
    isLogin.set(false)
    loginUser.set(null)
}

/**
 * 解析访问令牌(token)
 * @param access String
 */
fun parseToken(access: String): Long {
    if (TextUtils.isEmpty(access)) return 0
    val array = access.split(".")
    if (array.size < 2) {
        return 0
    }
    val base64Json = String(Base64.decode(array[1], Base64.DEFAULT))
    if (TextUtils.isEmpty(base64Json)) return 0
    val tokenBean = Gson().fromJson<TokenBean>(base64Json, TokenBean::class.java)
    return tokenBean.exp
}