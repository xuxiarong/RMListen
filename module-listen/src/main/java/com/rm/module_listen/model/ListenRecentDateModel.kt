package com.rm.module_listen.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_listen.R

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
data class ListenRecentDateModel constructor(
    val date: String = "",
    val showDelete : Boolean = false,
    val showSearch : Boolean = false
) : MultiItemEntity{
    override val itemType= R.layout.listen_item_recent_date
}
