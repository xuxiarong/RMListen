package com.rm.module_download.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.module_download.viewmodel.DownloadChapterSelectionViewModel

class DownloadChapterSelectionAdapter<DownloadChapterItemBean> constructor(
    viewModel: DownloadChapterSelectionViewModel,
    data: MutableList<DownloadChapterItemBean>,
    itemLayoutId: Int,
    viewModelBrId: Int,
    dataBrId: Int
) : BaseBindVMAdapter<DownloadChapterItemBean>(viewModel, data, itemLayoutId, viewModelBrId, dataBrId) {

    val mViewModel = viewModel

    val checkedList: MutableList<DownloadChapterItemBean> by lazy { mutableListOf<DownloadChapterItemBean>() }


    override fun convert(holder: BaseViewHolder, item: DownloadChapterItemBean) {
        super.convert(holder, item)
    }

}