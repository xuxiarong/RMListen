package com.rm.module_home.model.home.collect

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeMenuModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeMenuRvModel(val menuList : MutableList<HomeMenuModel>) : MultiItemEntity {
    override val itemType = R.layout.home_item_menu_rv
}