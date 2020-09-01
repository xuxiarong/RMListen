package com.rm.module_login.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_login.bean.LoginInfo
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * desc   : Login网络请求
 * date   : 2020/08/04
 * version: 1.0
 */
interface LoginApiService {
    /**
     * 验证码登陆
     * @param account String
     * @param code String
     */
    @POST("auth/token/sms")
    suspend fun loginByVerifyCode(
        @Query("account") account: String,
        @Query("code") code: String
    ): BaseResponse<LoginInfo>
}