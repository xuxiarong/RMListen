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
    val book_name: String,//书名
    val author: String,//作者名
    val anchor_name: String,//播音员名称
    val contact: String,//联系方式
    val device_id: String = DeviceUtils.uniqueDeviceId//设备id
)