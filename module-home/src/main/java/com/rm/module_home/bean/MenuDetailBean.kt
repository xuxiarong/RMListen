package com.rm.module_home.bean

import com.rm.business_lib.bean.BookBean

data class MenuDetailBean(
    val title: String,//标题
    val frontCover: String,//封面
    val authorName: String,//作者名
    val authorIcon: String,//作者头像
    val totalNumber: String,//总阅读数
    val brief: String,//描述
    val isCollected: Boolean = false,//是否收藏
    val detailList: MutableList<BookBean>
) {
    companion object {
        const val CORNER = 14f
    }
}
