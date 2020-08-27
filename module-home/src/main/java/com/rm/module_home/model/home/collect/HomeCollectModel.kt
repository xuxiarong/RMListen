package com.rm.module_home.model.home.collect

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeCollectModel constructor(val imageId : Int, val collectName:String) : BaseMultiAdapter.IBindItemType {
    override fun bindType(): Int {
        return R.layout.home_item_collect
    }
}