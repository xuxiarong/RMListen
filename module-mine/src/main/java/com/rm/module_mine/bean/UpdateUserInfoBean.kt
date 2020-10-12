package com.rm.module_mine.bean

/**
 *
 * @author yuanfang
 * @date 10/9/20
 * @description
 *
 */
data class UpdateUserInfoBean(
    val nickname: String,//昵称
    val gender: String,//性别
    val birthday: String,//生日(1970-01-01)
    val address: String,//地址
    val signature: String//个性签名
)
