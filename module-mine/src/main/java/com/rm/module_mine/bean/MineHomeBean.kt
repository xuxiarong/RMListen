package com.rm.module_mine.bean

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
data class MineHomeBean(
    var title: String,
    val data: MutableList<MineHomeDetailBean>
)

data class MineHomeDetailBean(
    var iconRes: Int,
    var name: String,
    var type: Int
)