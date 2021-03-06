package com.rm.baselisten.adapter.multi

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.mvvm.BaseViewModel


/**
 * desc   : MVVM模式抽象的多Item基类BaseAdapter
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseMultiVMAdapter<T : MultiItemEntity> constructor(
    val viewModel: BaseViewModel,
    private val viewModelBrId: Int,
    dataBrId: Int
) : BaseMultiAdapter<T>(dataBrId) {

    override fun convert(holder: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.setVariable(viewModelBrId,viewModel)
    }
}