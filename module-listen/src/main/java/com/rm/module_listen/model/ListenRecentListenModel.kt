package com.rm.module_listen.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_listen.R

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
data class ListenRecentListenModel constructor(
    val imageUrl: String,
    val iconCorner: Float,
    val title: String,
    val chapter: String,
    val tag: String,
    val time : String,
    val status : String
):MultiItemEntity {
    override val itemType = R.layout.listen_item_recent_listen
}
