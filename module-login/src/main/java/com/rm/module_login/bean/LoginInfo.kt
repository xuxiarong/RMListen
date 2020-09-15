package com.rm.module_login.bean

import com.rm.business_lib.bean.LoginUserBean

/**
 * desc   : 登录信息
 * date   : 2020/09/01
 * version: 1.0
 */
data class LoginInfo(val access: String, val refresh: String, val member: LoginUserBean)