package com.rm.module_play.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_play.adapter.BookPlayerAdapter

/**
 *精品阅读
 * @des:
 * @data: 9/9/20 5:42 PM
 * @Version: 1.0.0
 */
class PlayControlRecommentModel(override val itemType: Int= BookPlayerAdapter.ITEM_TYPE_BOUTIQUE) :
    MultiItemEntity {
}