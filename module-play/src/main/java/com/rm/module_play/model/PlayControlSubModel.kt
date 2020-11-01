package com.rm.module_play.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.business_lib.bean.Anchor
import com.rm.module_play.adapter.BookPlayerAdapter

/**
 *订阅栏目
 * @des:
 * @data: 9/9/20 5:39 PM
 * @Version: 1.0.0
 */
class PlayControlSubModel(var anchor : Anchor = Anchor.getDefault(), override val itemType: Int= BookPlayerAdapter.ITEM_TYPE_ANCHOR) :
    MultiItemEntity {


}