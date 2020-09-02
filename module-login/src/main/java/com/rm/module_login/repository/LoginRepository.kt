package com.rm.module_login.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.baselisten.util.getStringMMKV
import com.rm.business_lib.REFRESH_TOKEN
import com.rm.module_login.api.LoginApiService
import com.rm.module_login.bean.LoginInfo

/**
 * desc   : 登陆Repository
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginRepository(private val apiService: LoginApiService) : BaseRepository() {

    /**
     * 发送登陆短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun sendLoginVerifyCode(area_code: String, phone: String): BaseResult<Any> {
        return apiCall { apiService.sendMessage("login", area_code, phone) }
    }

    /**
     * 发送忘记密码短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun sendForgetPasswordVerifyCode(area_code: String, phone: String): BaseResult<Any> {
        return apiCall { apiService.sendMessage("forget_pwd", area_code, phone) }
    }

    /**
     * 发送重新绑定短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun sendRebindVerifyCode(area_code: String, phone: String): BaseResult<Any> {
        return apiCall { apiService.sendMessage("rebind_phone", area_code, phone) }
    }

    /**
     * 短信验证码登陆
     * @param phone String
     * @param code String
     * @return BaseResult<LoginInfo>
     */
    suspend fun loginByVerifyCode(phone: String, code: String): BaseResult<LoginInfo> {
        return apiCall { apiService.loginByVerifyCode(phone, code) }
    }

    /**
     * 刷新token
     * @param refreshToken String
     * @return BaseResult<LoginInfo>
     */
    suspend fun refreshToken(refreshToken: String): BaseResult<LoginInfo> {
        return apiCall { apiService.refreshToken(REFRESH_TOKEN.getStringMMKV()) }
    }

}