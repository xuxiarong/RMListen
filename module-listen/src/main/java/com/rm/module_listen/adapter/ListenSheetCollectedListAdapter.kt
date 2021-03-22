package com.rm.module_listen.adapter

import android.view.View
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
        val tipView = holder.getView<View>(R.id.listen_sheet_collected_adapter_no_book)
        val rv = holder.getView<RecyclerView>(R.id.listen_sheet_collected_adapter_recycler_view)
        item.audio_list?.let {
            val list = if (it.size > 3) {
                it.subList(0, 3)
            } else {
                it
            }
            if (list.isNotEmpty()) {
                tipView.visibility = View.GONE
                rv.visibility = View.VISIBLE
                rv.apply {
                    val adapter = CommonBindVMAdapter(
                        viewModel,
                        list,
                        R.layout.listen_adapter_sheet_collected_book,
                        BR.viewModel,
                        BR.bookBean
                    )
                    bindGridLayout(adapter, 3)
                    if (rv.itemDecorationCount == 0) {
                        gridItemDecoration(8f)
                    }
                }

            } else {
                rv.visibility = View.GONE
                tipView.visibility = View.VISIBLE
            }
        }
    }
}