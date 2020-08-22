package com.rm.module_home.model

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeRecommendModel constructor(
    val url: String,
    val iconCorner : Float,
    val name: String,
    val author: String,
    val tag: String,
    val itemClick: () -> Unit
) : BaseMultiAdapter.IBindItemType {
    override fun bindType(): Int {
        return R.layout.home_item_recommend
    }
}