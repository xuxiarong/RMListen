package com.rm.module_listen.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.module_listen.R

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */

data class ListenHistoryModel constructor(
    val audio : ListenAudioEntity
) : MultiItemEntity {
    override var itemType = R.layout.listen_item_recent_listen
}