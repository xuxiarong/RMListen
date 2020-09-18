package com.rm.module_home.model.home.collect

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeCollectModel constructor(val imageId : Int, val collectName:String) : MultiItemEntity {
    override val itemType = R.layout.home_item_menu

}