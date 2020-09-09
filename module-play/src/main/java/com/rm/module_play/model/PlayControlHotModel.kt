package com.rm.module_play.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_play.adapter.BookPlayerAdapter

/**
 *
 * @des:热门主播关注
 * @data: 9/9/20 5:42 PM
 * @Version: 1.0.0
 */
class PlayControlHotModel(override val itemType: Int= BookPlayerAdapter.ITEM_TYPE_FOLLOW) :
    MultiItemEntity {

}