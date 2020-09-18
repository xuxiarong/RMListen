package com.rm.module_home.model.home.grid

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeAudioModel

/**
 * desc   :
 * date   : 2020/08/25
 * version: 1.0
 */
class HomeGridRecommendModel constructor(var gridRecommendRvModel: HomeAudioModel) : MultiItemEntity {
    override val itemType = R.layout.home_item_recommend_grid
}