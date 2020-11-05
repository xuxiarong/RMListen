package com.rm.module_home.bean

import com.google.gson.annotations.SerializedName
import com.rm.business_lib.bean.BannerInfoBean

/**
 * desc   : 类别列表bean
 * date   : 2020/09/19
 * version: 1.0
 */
data class CategoryTabListBean(
    @SerializedName("page_id")
    var page_id: Int,
    @SerializedName("page_name")
    var page_name: String,
    @SerializedName("banner_list")
    var banner_list: List<BannerInfoBean> = listOf(),
    @SerializedName("class_list")
    var class_list: List<CategoryTabBean> = listOf()
)