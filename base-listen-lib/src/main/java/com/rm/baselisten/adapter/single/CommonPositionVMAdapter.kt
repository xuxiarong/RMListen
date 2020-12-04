package com.rm.baselisten.adapter.single

import android.annotation.SuppressLint
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.viewmodel.BaseVMViewModel

/**
 * desc   : MVVM通用的单Item的Adapter
 * date   : 2020/08/27
 * version: 1.0
 */
open class CommonPositionVMAdapter<T> constructor(
    viewModel: BaseVMViewModel,
    commonData: MutableList<T>,
    commonItemLayoutId: Int,
    commonViewModelId : Int,
    commonDataBrId: Int,
    val commonPositionViewId : Int
) : BaseBindVMAdapter<T>(viewModel,commonData, commonItemLayoutId, commonViewModelId,commonDataBrId){
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: T) {
        super.convert(holder, item)
        holder.getView<TextView>(commonPositionViewId).text = "${holder.adapterPosition+1}"
    }
}





