package com.rm.module_home.model.home.horsingle

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeAudioModel

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
data class HomeRecommendHorSingleModel constructor(
    val singleModel: HomeAudioModel
) : MultiItemEntity {
    override val itemType = R.layout.home_item_recommend_hor_single
}