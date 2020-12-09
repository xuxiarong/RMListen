package com.rm.business_lib.bean

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
data class BusinessVersionUrlBean(
    val version: String?,//版本号
    val package_name: String?,//程序包名
    val package_url: String?,//包下载地址
    val created_at: String?,//上传日期
    val type: String?,//更新类型，1普通更新，2强制更新
    val description: String?//更新日志
)