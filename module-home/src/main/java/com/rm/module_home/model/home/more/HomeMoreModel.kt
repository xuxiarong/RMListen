package com.rm.module_home.model.home.more

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeMoreModel constructor(val title : String, var moreClick : () -> Unit) :
    MultiItemEntity { override val itemType = R.layout.home_item_more
}