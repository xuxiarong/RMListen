package com.rm.business_lib.bean

import android.text.TextUtils

/**
 * desc   : 登陆用户个人基本信息
 * date   : 2020/09/03
 * version: 1.0
 */
data class LoginUserBean(
    val id: String,  // id
    val account: String, // 账户
    val nickname: String, // 昵称
    val gender: String, //性别 0=未知/保密，1=男，2=女
    val birthday: String, // 生日
    val address: String, // 地址
    val signature: String, // 个性签名
    val avatar: String, // 头像路径
    val avatar_url: String, // 头像链接
    val fans: String, // 粉丝数
    val follows: String // 关注数
) {
    fun getSexStr(): String {
        return when (gender) {
            "0" -> {
                "保密"
            }
            "1" -> {
                "男"
            }
            "2" -> {
                "女"
            }
            else -> {
                "未填写"
            }
        }
    }

    fun getNickNameStr(): String {
        return if (TextUtils.isEmpty(nickname)) "未填写" else nickname
    }

    fun getAddressStr(): String {
        return if (TextUtils.isEmpty(address)) "未填写" else address
    }

    fun getSignatureStr(): String {
        return if (TextUtils.isEmpty(signature)) "未填写" else signature
    }

    fun getBirthdayStr(): String {
        return if (TextUtils.isEmpty(birthday)) "未填写" else birthday
    }
}