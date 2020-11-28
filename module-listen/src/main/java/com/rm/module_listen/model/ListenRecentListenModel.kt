package com.rm.module_listen.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.module_listen.R

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */

data class ListenHistoryModel constructor(
    val audio : ListenAudioEntity,
    val chapter: ListenChapterEntity
) : MultiItemEntity {
    override var itemType = R.layout.listen_item_history_listen
}
