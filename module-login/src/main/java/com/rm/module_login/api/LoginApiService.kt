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

    /**
     * 密码登陆
     * @param account String
     * @param code String
     */
    @POST("auth/token/pwd")
    suspend fun loginByPassword(
        @Query("account") account: String,
        @Query("password") code: String
    ): BaseResponse<LoginInfo>

    /**
     * 刷新token(令牌)
     * @param account String
     * @param code String
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Query("refresh_token") refreshToken: String
    ): BaseResponse<LoginInfo>


    /**
     * 发送短信验证码
     * @param type String  发送短信类型，"login" = 登陆，"forget_pwd" = 忘记密码,"rebind_phone" = 重新绑定手机
     * @param area_code String 手机所在区号(例：+86)
     * @param phone String 接收短信验证码的手机号码
     * @return BaseResponse<*>
     */
    @POST("sms/code")
    suspend fun sendMessage(
        @Query("type") type: String,
        @Query("area_code") area_code: String,
        @Query("phone") phone: String
    ): BaseResponse<Any>
}