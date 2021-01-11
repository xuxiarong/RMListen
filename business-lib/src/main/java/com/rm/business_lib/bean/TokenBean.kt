package com.rm.business_lib.bean

/**
 * desc   : 解析token的bean
 * date   : 2020/09/17
 * version: 1.0
 */
data class TokenBean(
    private val iat: Long,
    private val nbf: Long,
    val exp: Long, // token 过期时间戳
    val jti: String,
    val typ: String,
    val uid: String
)