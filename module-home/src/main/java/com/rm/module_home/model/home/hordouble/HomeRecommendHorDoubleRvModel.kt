package com.rm.module_home.model.home.hordouble

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
class HomeRecommendHorDoubleRvModel : BaseMultiAdapter.IBindItemType{
    override fun bindType(): Int {
        return R.layout.home_item_recommend_hor_double_rv
    }
}