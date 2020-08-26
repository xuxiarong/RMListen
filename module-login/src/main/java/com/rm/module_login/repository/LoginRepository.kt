package com.rm.module_login.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.module_login.api.LoginApiService

/**
 * desc   : 登陆Repository
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginRepository(private val apiService: LoginApiService) : BaseRepository() {
}