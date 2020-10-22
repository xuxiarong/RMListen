package com.rm.module_listen.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindGridLayout
import com.rm.baselisten.binding.gridItemDecoration
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.SheetFavorDataBean

class ListenSheetCollectedListAdapter(
    viewModel: BaseVMViewModel
) : BaseBindVMAdapter<SheetFavorDataBean>(
    viewModel,
    mutableListOf(),
    R.layout.listen_adapter_sheet_collected_list,
    BR.viewModel,
    BR.item
) {
    override fun convert(holder: BaseViewHolder, item: SheetFavorDataBean) {
        super.convert(holder, item)
        item.audio_list?.let {
            val list = if (it.size > 3) {
                it.subList(0, 3)
            } else {
                it
            }
            holder.getView<RecyclerView>(R.id.listen_sheet_collected_adapter_recycler_view)
                .apply {
                    bindGridLayout(
                        CommonBindVMAdapter(
                            viewModel,
                            list,
                            R.layout.listen_adapter_sheet_collected_book,
                            BR.viewModel,
                            BR.bookBean
                        )
                        , 3
                    )
                    gridItemDecoration(8f)
                }
        }
    }
}