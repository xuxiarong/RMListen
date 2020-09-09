package com.rm.module_play.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_play.adapter.BookPlayerAdapter

/**
 *播放控制器
 * @des:
 * @data: 9/9/20 5:38 PM
 * @Version: 1.0.0
 */

class PlayControlModel(override val itemType: Int= BookPlayerAdapter.ITEM_TYPE_PLAYER) :
    MultiItemEntity {
}