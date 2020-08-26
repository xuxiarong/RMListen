package com.rm.module_home.model.home.grid

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.module_home.R
import com.rm.module_home.model.HomeRecommendModel

/**
 * desc   :
 * date   : 2020/08/25
 * version: 1.0
 */
class HomeGridRecommendModel constructor(var gridRecommendRvModel : HomeRecommendModel): BaseMultiAdapter.IBindItemType{
    override fun bindType(): Int {
        return R.layout.home_item_recommend_grid
    }

}