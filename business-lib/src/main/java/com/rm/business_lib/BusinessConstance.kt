package com.rm.business_lib

import androidx.lifecycle.MutableLiveData
import com.rm.business_lib.bean.LoginUserBean

/**
 * desc   : 基础业务常量类
 * date   : 2020/09/01
 * version: 1.0
 */
// 访问令牌(token)
const val ACCESS_TOKEN = "accessToken"

// 刷新令牌(token)
const val REFRESH_TOKEN = "refreshToken"

// 当前登陆用户信息
const val LOGIN_USER_INFO = "loginUserInfo"

// 当前是否登陆
var isLogin = MutableLiveData<Boolean>(false)

// 当前登陆的用户信息
var loginUser = MutableLiveData<LoginUserBean>()

//是否是第首次收藏
const val IS_FIRST_FAVORITES = "is_first_favorites"

//是否是首次添加听单
const val IS_FIRST_ADD_SHEET = "is_first_add_sheet"