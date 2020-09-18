package com.rm.module_play.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_play.adapter.BookPlayerAdapter

/**
 *评论标题
 * @des:
 * @data: 9/9/20 5:44 PM
 * @Version: 1.0.0
 */
class PlayControlCommentTitleModel(
    override val itemType: Int = BookPlayerAdapter.ITEM_TYPE_COMMENR_TITLE
) :
    MultiItemEntity {

}