package com.rm.module_login.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_login.api.LoginApiService
import com.rm.module_login.bean.LoginInfo

/**
 * desc   : 登陆Repository
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginRepository(private val apiService: LoginApiService) : BaseRepository() {
    suspend fun loginByVerifyCode(phone: String, code: String): BaseResult<LoginInfo> {
        return apiCall { apiService.loginByVerifyCode(phone, code) }

    }
}