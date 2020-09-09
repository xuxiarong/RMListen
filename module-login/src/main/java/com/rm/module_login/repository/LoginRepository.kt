package com.rm.module_login.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.baselisten.util.getStringMMKV
import com.rm.business_lib.REFRESH_TOKEN
import com.rm.business_lib.utils.aes.AESUtil
import com.rm.module_login.api.LoginApiService
import com.rm.module_login.bean.LoginInfo
import com.rm.module_login.bean.ValidateCodeBean

/**
 * desc   : 登陆Repository
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginRepository(private val apiService: LoginApiService) : BaseRepository() {

    // 密码加密所需key
    private val KEY = "a48435c7e0930cd2c5f2a0c8a3ec29fe"

    // 密码加密所需iv
    private val IV = "140fa03a972cdb3c"

    /**
     * 去掉 "+"
     * @param area_code String
     */
    private fun subPlusChar(area_code: String): String {
        if (!area_code.startsWith("+")) {
            return area_code
        }
        return area_code.substring(1, area_code.length)
    }

    /**
     * 发送登陆短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun sendLoginVerifyCode(area_code: String, phone: String): BaseResult<Any> {
        return apiCall { apiService.sendMessage("login", subPlusChar(area_code), phone) }
    }


    /**
     * 发送忘记密码短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun sendForgetPasswordVerifyCode(area_code: String, phone: String): BaseResult<Any> {
        return apiCall { apiService.sendMessage("forget_pwd", subPlusChar(area_code), phone) }
    }

    /**
     * 发送重新绑定短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun sendRebindVerifyCode(area_code: String, phone: String): BaseResult<Any> {
        return apiCall { apiService.sendMessage("rebind_phone", subPlusChar(area_code), phone) }
    }

    /**
     * 短信验证码登陆
     * @param area_code String
     * @param phone String
     * @param code String
     * @return BaseResult<LoginInfo>
     */
    suspend fun loginByVerifyCode(
        area_code: String,
        phone: String,
        code: String
    ): BaseResult<LoginInfo> {
        return apiCall { apiService.loginByVerifyCode(subPlusChar(area_code), phone, code) }
    }

    /**
     * 校验忘记密码验证码是否正确
     * @param phone String
     * @param code String
     * @return BaseResult<LoginInfo>
     */
    suspend fun validateForgetPasswordVerifyCode(
        area_code: String,
        phone: String,
        code: String
    ): BaseResult<ValidateCodeBean> {
        return apiCall {
            apiService.validateCode(
                "forget_pwd",
                subPlusChar(area_code),
                phone,
                code
            )
        }
    }

    /**
     * 密码登陆
     * @param area_code String
     * @param phone String
     * @param password String
     * @return BaseResult<LoginInfo>
     */
    suspend fun loginByPassword(
        area_code: String,
        phone: String,
        password: String
    ): BaseResult<LoginInfo> {
        return apiCall {
            apiService.loginByPassword(
                subPlusChar(area_code),
                phone,
                AESUtil.encryptString2Base64(password, KEY, IV)
            )
        }
    }

    /**
     * 刷新token
     * @param refreshToken String
     * @return BaseResult<LoginInfo>
     */
    suspend fun refreshToken(refreshToken: String): BaseResult<LoginInfo> {
        return apiCall { apiService.refreshToken(REFRESH_TOKEN.getStringMMKV()) }
    }


    /**
     * 通过验证码重设密码
     * @param area_code String
     * @param phone String
     * @param code String
     * @param password String
     * @return BaseResult<Any>
     */
    suspend fun resetPasswordByVerifyCode(
        area_code: String,
        phone: String,
        code: String,
        password: String
    ): BaseResult<Any> {
        return apiCall {
            apiService.resetPasswordByVerifyCode(
                subPlusChar(area_code),
                phone,
                code,
                AESUtil.encryptString2Base64(password, KEY, IV)
            )
        }
    }

    /**
     * 通过旧密码重设密码
     * @param password String
     * @param newPassword String
     * @return BaseResult<Any>
     */
    suspend fun resetPasswordByVerifyCode(
        password: String,
        newPassword: String
    ): BaseResult<Any> {
        return apiCall {
            apiService.resetPasswordByOldPassword(
                AESUtil.encryptString2Base64(password, KEY, IV),
                AESUtil.encryptString2Base64(newPassword, KEY, IV)
            )
        }
    }

}