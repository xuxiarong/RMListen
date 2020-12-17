package com.rm.module_mine.bean

import com.rm.business_lib.utils.DeviceUtils

/**
 *
 * @author yuanfang
 * @date 12/5/20
 * @description
 *
 */
data class MineGetBookBean(
    val book_name: String,
    val author: String,
    val anchor_name: String,
    val contact: String,
    val device_id: String = DeviceUtils.uniqueDeviceId
)