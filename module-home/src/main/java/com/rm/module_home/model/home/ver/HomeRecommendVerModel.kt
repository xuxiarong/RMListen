package com.rm.module_home.model.home.ver

import com.rm.baselisten.adapter.multi.BaseMultiAdapter
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
data class HomeRecommendVerModel constructor(
    var imageUrl : String,
    val tag : String,
    val title : String,
    val content : String,
    val author : String
) : BaseMultiAdapter.IBindItemType {
    override fun bindType(): Int {
        return R.layout.home_item_recommend_ver
    }
}