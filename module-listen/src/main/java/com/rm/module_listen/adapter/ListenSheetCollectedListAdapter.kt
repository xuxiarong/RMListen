package com.rm.module_listen.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.holder.BaseBindHolder
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.ListenSheetCollectedDataBean

class ListenSheetCollectedListAdapter(
    viewModel: BaseVMViewModel
) : BaseBindVMAdapter<ListenSheetCollectedDataBean>(
    viewModel,
    mutableListOf(),
    R.layout.listen_adapter_sheet_collected_list,
    BR.viewModel,
    BR.item
) {
    override fun convert(holder: BaseViewHolder, item: ListenSheetCollectedDataBean) {
        super.convert(holder, item)
        item.audio_list?.let {
            val recyclerView =
                holder.getView<RecyclerView>(R.id.listen_sheet_collected_adapter_recycler_view)
            recyclerView.apply {
                bindGridLayout(
                    CommonBindVMAdapter(
                        viewModel,
                        it,
                        R.layout.listen_adapter_sheet_collected_book,
                        BR.viewModel,
                        BR.bookBean
                    )
                ,3)
            }
        }
    }
}