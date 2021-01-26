package com.rm.module_listen.adapter

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.module_listen.R
import com.rm.module_listen.viewmodel.ListenRecentListenViewModel

/**
 *
 * @author yuanfang
 * @date 1/26/21
 * @description
 *
 */
class ListenRecentListenAdapter(
    playViewModel: ListenRecentListenViewModel,
    modelBrId: Int,
    itemBrId: Int
) : BaseMultiVMAdapter<MultiItemEntity>(playViewModel, modelBrId, itemBrId) {

    class ListenRecentListenContentItemEntity(
        val audio: ListenAudioEntity,
        val chapter: ListenChapterEntity
    ) : MultiItemEntity {
        override val itemType: Int = R.layout.listen_item_recent_listen
    }

    class ListenRecentListenOtherItemEntity : MultiItemEntity {
        override val itemType: Int = R.layout.listen_item_recent_date
    }
}