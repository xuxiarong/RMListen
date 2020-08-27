package com.rm.baselisten.adapter.multi

import com.rm.baselisten.holder.BaseBindHolder
import com.rm.baselisten.mvvm.BaseViewModel


/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */
abstract class BaseMultiVMAdapter<T : BaseMultiAdapter.IBindItemType> constructor(
    val viewModel: BaseViewModel,
    vmList: List<T>,
    private val viewModelBrId: Int,
    dataBrId: Int
) : BaseMultiAdapter<T>(vmList, dataBrId) {

    override fun convert(holder: BaseBindHolder, position: Int) {
        holder.binding.setVariable(viewModelBrId,viewModel)
    }
}