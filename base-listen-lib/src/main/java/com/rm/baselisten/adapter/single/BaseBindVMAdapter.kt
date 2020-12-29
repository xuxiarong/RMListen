package com.rm.baselisten.adapter.single

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.viewmodel.BaseVMViewModel


/**
 * desc   : MVVM模式抽象的单Item基类BaseAdapter
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseBindVMAdapter<T> constructor(var viewModel : BaseVMViewModel, vmData: MutableList<T>, vmLayoutId: Int, private var viewModelBrId : Int, vmDataBrId: Int) :
    BaseBindAdapter<T>(vmData,vmLayoutId,vmDataBrId) {

     override fun convert(holder: BaseViewHolder, item: T){
         DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.setVariable(viewModelBrId,viewModel)
     }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.unbind()
    }
}