package com.rm.baselisten.adapter.single

import com.rm.baselisten.holder.BaseBindHolder
import com.rm.baselisten.viewmodel.BaseVMViewModel


/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseBindVMAdapter constructor(var viewModel : BaseVMViewModel, vmData: List<*>?, vmLayoutId: Int, private var viewModelBrId : Int, vmDataBrId: Int) :
    BaseBindAdapter(vmData,vmLayoutId,vmDataBrId) {

     override fun convert(holder: BaseBindHolder, position: Int){
         holder.binding.setVariable(viewModelBrId,viewModel)
     }
}