package com.rm.module_play.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.business_lib.bean.HomeDetailList
import com.rm.module_play.adapter.BookPlayerAdapter

/**
 *订阅栏目
 * @des:
 * @data: 9/9/20 5:39 PM
 * @Version: 1.0.0
 */
class PlaySubAudioModel(var audio : HomeDetailList = HomeDetailList.getDefault(), override val itemType: Int= BookPlayerAdapter.ITEM_TYPE_AUDIO_SUB) :
    MultiItemEntity {


}