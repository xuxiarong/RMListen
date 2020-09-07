package com.rm.business_lib.bean

/**
 * desc   : 列表书籍item bean
 * date   : 2020/08/21
 * version: 1.0
 */
data class BookBean(
    val id: Int,
    val name: String = "",
    val icon: String = "",
    val tips: String = "",
    val describe: String = "",
    val author: String = ""
)