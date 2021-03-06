package com.rm.business_lib.bean

import com.rm.business_lib.BuildConfig


/**
 *
 * @author yuanfang
 * @date 12/5/20
 * @description
 *
 */
data class BusinessUpdateVersionBean(
    val version: String = BuildConfig.VERSION_NAME,
    val platform: String = "android"
)