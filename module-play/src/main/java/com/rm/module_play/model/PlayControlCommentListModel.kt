package com.rm.module_play.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_play.adapter.BookPlayerAdapter

/**
 *
 * @des:评论列表
 * @data: 9/9/20 5:44 PM
 * @Version: 1.0.0
 */
class PlayControlCommentListModel(override val itemType: Int= BookPlayerAdapter.ITEM_TYPE_COMMENR_LIST, var comments: Comments? = null) : MultiItemEntity {
}