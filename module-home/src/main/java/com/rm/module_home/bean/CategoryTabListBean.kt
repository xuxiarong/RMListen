package com.rm.module_home.bean

import com.rm.business_lib.bean.BannerInfoBean

/**
 * desc   : 类别列表bean
 * date   : 2020/09/19
 * version: 1.0
 */
data class CategoryTabListBean(
    val page_id: Int,
    val page_name: String,
    val banner_list: List<BannerInfoBean>,
    val class_list: List<CategoryTabBean>
)