package com.rm.baselisten.adapter.single

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.viewmodel.BaseVMViewModel
import java.lang.ref.WeakReference

/**
 * desc   : MVVM通用的单Item的Adapter
 * date   : 2020/08/27
 * version: 1.0
 */
open class WeakBindVMAdapter<T> constructor(var viewModel : WeakReference<BaseVMViewModel>, vmData: MutableList<T>, vmLayoutId: Int, private var viewModelBrId : Int, vmDataBrId: Int) :
        BaseBindAdapter<T>(vmData,vmLayoutId,vmDataBrId) {

    override fun convert(holder: BaseViewHolder, item: T){
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.setVariable(viewModelBrId,viewModel.get())
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.unbind()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        viewModel.clear()
    }

}





