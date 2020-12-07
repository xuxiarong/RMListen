package com.rm.module_play.adapter

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.module_play.R
import com.rm.module_play.model.Comments
import com.rm.module_play.model.PlayDetailAdvertiseModel
import com.rm.module_play.viewmodel.PlayViewModel

/**
 *
 * @author yuanfang
 * @date 12/7/20
 * @description
 *
 */
class PlayDetailCommentAdapter(
    playViewModel: PlayViewModel,
    modelBrId: Int,
    itemBrId: Int
) : BaseMultiVMAdapter<MultiItemEntity>(playViewModel, modelBrId, itemBrId) {

    class PlayDetailCommentItemEntity(val data: Comments) : MultiItemEntity {
        override val itemType: Int = R.layout.play_item_comment
    }
    class PlayDetailCommentAdvertiseItemEntity(val data: PlayDetailAdvertiseModel) : MultiItemEntity {
        override val itemType: Int = R.layout.play_item_comment_adver
    }
}