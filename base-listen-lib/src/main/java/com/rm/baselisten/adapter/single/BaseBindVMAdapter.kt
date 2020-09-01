package com.rm.baselisten.adapter.single

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rm.baselisten.holder.BaseBindHolder
import com.rm.baselisten.viewmodel.BaseVMViewModel


/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseBindVMAdapter<T> constructor(var viewModel : BaseVMViewModel, vmData: MutableList<T>, vmLayoutId: Int, private var viewModelBrId : Int, vmDataBrId: Int) :
    BaseBindAdapter<T>(vmData,vmLayoutId,vmDataBrId) {

     override fun convert(holder: BaseBindHolder, item: T){
         DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.setVariable(viewModelBrId,viewModel)
     }
}