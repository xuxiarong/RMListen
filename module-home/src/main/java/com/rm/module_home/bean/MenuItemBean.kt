package com.rm.module_home.bean

import com.rm.business_lib.bean.BookBean

/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
data class MenuItemBean(
    val name: String,
    val authorName: String,
    val authorIcon: String,
    val collectionNumber: String,
    val totalNumber: Int,
    val bookList: MutableList<BookBean>
){
    val isAuthorCircle = true
}