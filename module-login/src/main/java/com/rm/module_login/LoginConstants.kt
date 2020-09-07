package com.rm.module_login

import androidx.lifecycle.MutableLiveData
import com.rm.component_comm.login.bean.LoginUserBean

/**
 * desc   :
 * date   : 2020/09/01
 * version: 1.0
 */
object LoginConstants {

    const val LOGIN_LOGGING = 0
    const val LOGIN_SUCCESS = 1
    const val LOGIN_FAILED = 2

    // 当前是否登陆
    var isLogin = MutableLiveData<Boolean>(false)

    // 当前登陆的用户信息
    var loginUser = MutableLiveData<LoginUserBean>()
}