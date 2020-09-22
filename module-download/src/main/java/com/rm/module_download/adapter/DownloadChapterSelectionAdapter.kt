package com.rm.module_download.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel

class DownloadChapterSelectionAdapter<DownloadChapterItemBean> constructor(
    viewModel: BaseVMViewModel,
    data: MutableList<DownloadChapterItemBean>,
    itemLayoutId: Int,
    viewModelId : Int,
    dataBrId: Int
) : CommonBindVMAdapter<DownloadChapterItemBean>(viewModel,data, itemLayoutId, viewModelId,dataBrId){

     override fun convert(holder: BaseViewHolder, item: DownloadChapterItemBean) {
         super.convert(holder, item)
     }

 }