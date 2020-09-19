package com.rm.module_listen.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.ListenSheetCollectedDataBean

class ListenSheetCollectedListAdapter(
    viewModel: BaseVMViewModel,
    commonData: MutableList<ListenSheetCollectedDataBean>,
    commonViewModelId: Int,
    commonDataBrId: Int
) : BaseBindVMAdapter<ListenSheetCollectedDataBean>(
    viewModel,
    commonData,
    R.layout.listen_adapter_sheet_collected_list,
    commonViewModelId,
    commonDataBrId
) {
    override fun convert(holder: BaseViewHolder, item: ListenSheetCollectedDataBean) {
        super.convert(holder, item)
        holder.getView<RecyclerView>(R.id.listen_sheet_collected_adapter_recycler_view).apply {
            bindHorizontalLayout(mItemAdapter)
        }
        mItemAdapter.setList(item.audio_list)
    }

    private val mItemAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            viewModel,
            mutableListOf(),
            R.layout.listen_adapter_sheet_collected_book,
            BR.viewModel,
            BR.bookBean
        )
    }

}