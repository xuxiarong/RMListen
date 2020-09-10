package com.rm.component_comm

import androidx.lifecycle.MutableLiveData
import com.rm.component_comm.login.bean.LoginUserBean

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
val IS_LOGIN="IS_LOGIN"

// 当前是否登陆
var isLogin = MutableLiveData<Boolean>(false)

// 当前登陆的用户信息
var loginUser = MutableLiveData<LoginUserBean>()