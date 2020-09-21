package com.rm.module_home.model.home.ver

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeAudioModel

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
data class HomeAudioVerModel constructor(
    var verModel: HomeAudioModel
) : MultiItemEntity {
    override val itemType = R.layout.home_item_audio_ver
}