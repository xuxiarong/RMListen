package com.rm.module_mine.bean

/**
 *
 * @author yuanfang
 * @date 12/2/20
 * @description
 *
 */
data class MineAboutUsBean(
    val title: String?,//标题
    val jump_url: String?,//跳转链接
    var sub_title: String?,//副标题
    var showRed: Boolean = false
)