package com.rm.module_listen.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.multi.BaseMultiVMAdapter
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenItemSubsAudioEarlyRvBinding
import com.rm.module_listen.databinding.ListenItemSubsAudioTodayRvBinding
import com.rm.module_listen.databinding.ListenItemSubsAudioYesterdayRvBinding
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
            R.layout.listen_item_subs_audio_today_rv ->{
                val binding = DataBindingUtil.getBinding<ListenItemSubsAudioTodayRvBinding>(holder.itemView)
                binding?.listenItemTodayRv?.bindVerticalLayout(CommonMultiVMAdapter(
                    listenViewModel,
                    listenViewModel.todayUpdateList.value!!,
                    BR.viewModel,
                    BR.item
                ))
            }
            R.layout.listen_item_subs_audio_yesterday_rv ->{
                val binding = DataBindingUtil.getBinding<ListenItemSubsAudioYesterdayRvBinding>(holder.itemView)
                binding?.listenItemYesterdayRv?.bindVerticalLayout(CommonMultiVMAdapter(
                    listenViewModel,
                    listenViewModel.yesterdayUpdateList.value!!,
                    BR.viewModel,
                    BR.item
                ))
            }
            R.layout.listen_item_subs_audio_early_rv ->{
                val binding = DataBindingUtil.getBinding<ListenItemSubsAudioEarlyRvBinding>(holder.itemView)
                binding?.listenItemEarlyRv?.bindVerticalLayout(CommonMultiVMAdapter(
                    listenViewModel,
                    listenViewModel.earlyUpdateList.value!!,
                    BR.viewModel,
                    BR.item
                ))
            }
        }
    }
}