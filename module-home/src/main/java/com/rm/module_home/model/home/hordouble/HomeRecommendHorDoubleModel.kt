package com.rm.module_home.model.home.hordouble

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeAudioModel

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
data class HomeRecommendHorDoubleModel(
    val topRecommendModel: HomeAudioModel,
    val bottomRecommendModel: HomeAudioModel,
    var needShowBottom : Boolean = true
) : MultiItemEntity {
    override val itemType = R.layout.home_item_recommend_hor_double
}