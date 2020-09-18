package com.rm.module_home.model.home.more

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeBlockModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeMoreModel constructor(var block : HomeBlockModel) :
    MultiItemEntity { override val itemType = R.layout.home_item_more
}