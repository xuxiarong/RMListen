package com.rm.module_home.model

import com.rm.baselisten.adapter.BaseBindAdapter
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeCollectModel constructor(val imageId : Int, val collectName:String,var itemClick : () -> Unit) : BaseBindAdapter.IBindItemType {
    override fun bindType(): Int {
        return R.layout.home_item_collect
    }
}