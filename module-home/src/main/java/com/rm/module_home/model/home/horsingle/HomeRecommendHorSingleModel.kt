package com.rm.module_home.model.home.horsingle

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.module_home.R
import com.rm.module_home.model.HomeRecommendModel

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
data class HomeRecommendHorSingleModel constructor(
    val singleModel: HomeRecommendModel
) : BaseMultiAdapter.IBindItemType {
    override fun bindType(): Int {
        return R.layout.home_item_recommend_hor_single
    }
}