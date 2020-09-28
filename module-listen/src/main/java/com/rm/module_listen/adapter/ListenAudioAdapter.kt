package com.rm.module_listen.adapter

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.module_listen.viewmodel.ListenSubsUpdateViewModel

/**
 * desc   :
 * date   : 2020/09/15
 * version: 1.0
 */
class ListenAudioAdapter constructor(
    private var listenViewModel: ListenSubsUpdateViewModel,
    listenViewModelBrId: Int,
    listenItemBrId: Int
) : BaseMultiVMAdapter<MultiItemEntity>(listenViewModel, listenViewModelBrId, listenItemBrId) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (getItemViewType(position)) {

        }
    }
}