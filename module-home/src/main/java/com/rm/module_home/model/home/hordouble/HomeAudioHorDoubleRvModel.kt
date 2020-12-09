package com.rm.module_home.model.home.hordouble

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeBlockModel

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
data class HomeAudioHorDoubleRvModel constructor(
        val block : HomeBlockModel,
        var horDoubleList: ArrayList<MultiItemEntity>
        ) : MultiItemEntity {
    override val itemType = R.layout.home_item_audio_hor_double_rv
}
