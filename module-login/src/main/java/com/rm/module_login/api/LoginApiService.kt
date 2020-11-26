package com.rm.module_login.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_login.bean.LoginInfo
import com.rm.module_login.bean.ValidateCodeBean
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


/**
 * desc   : Login网络请求
 * date   : 2020/08/04
 * version: 1.0
 */
interface LoginApiService {
    /**
     * 验证码登陆
     * @param area_code String
     * @param account String
     * @param code String
     */
    @POST("auth/token/sms")
    suspend fun loginByVerifyCode(
        @Query("area_code") area_code: String,
        @Query("account") account: String,
        @Query("code") code: String
    ): BaseResponse<LoginInfo>

    /**
     * 密码登陆
     * @param account String
     * @param code String
     * @param area_code String  手机所在国家代码 不用传 "+"
     */
    @POST("auth/token/pwd")
    suspend fun loginByPassword(
        @Query("area_code") area_code: String,
        @Query("account") account: String,
        @Query("password") code: String
    ): BaseResponse<LoginInfo>

    /**
     * 刷新token(令牌)
     * @param refreshToken String
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

    /**
     * 校验验证码是否正确
     * @param type String  发送短信类型，"login" = 登陆，"forget_pwd" = 忘记密码,"rebind_phone" = 重新绑定手机 "close_account"= 销户
     * @param area_code String 手机所在区号(例：+86)
     * @param phone String 接收短信验证码的手机号码
     * @param code String 验证码
     * @return BaseResponse<*>
     */
    @POST("sms/validate")
    suspend fun validateCode(
        @Query("type") type: String,
        @Query("area_code") area_code: String,
        @Query("phone") phone: String,
        @Query("code") code: String
    ): BaseResponse<ValidateCodeBean>

    /**
     * 根据验证码重设密码
     * @param area_code String
     * @param phone String
     * @param code String
     * @param password String
     * @return BaseResponse<Any>
     */
    @POST("member/forget-password")
    suspend fun resetPasswordByVerifyCode(
        @Query("area_code") area_code: String,
        @Query("account") phone: String,
        @Query("code") code: String,
        @Query("password") password: String
    ): BaseResponse<Any>

    /**
     * 根据旧密码重设密码
     * @param password String
     * @param new_password String
     * @return BaseResponse<Any>
     */
    @POST("member/change-password")
    suspend fun resetPasswordByOldPassword(
        @Query("password") password: String,
        @Query("new_password") new_password: String
    ): BaseResponse<Any>

    /**
     * 注销账户
     * @param code String
     * @return BaseResponse<Any>
     */
    @PUT("member/close-account")
    suspend fun logout(@Query("code") code: String): BaseResponse<Any>


}