package com.rm.module_home.model.home.floor

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeAudioModel

/**
 * desc   :
 * date   : 2020/12/08
 * version: 1.0
 */
data class HomeFloorAudioModel constructor(var homeFloorModel: HomeAudioModel) : MultiItemEntity {
    override val itemType = R.layout.home_item_audio_grid
}