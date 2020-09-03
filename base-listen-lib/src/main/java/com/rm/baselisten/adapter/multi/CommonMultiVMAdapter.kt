package com.rm.baselisten.adapter.multi

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.mvvm.BaseViewModel

/**
 * desc   : MVVM通用的多Item的Adapter
 * date   : 2020/08/24
 * version: 1.0
 */
class CommonMultiVMAdapter(commonModel : BaseViewModel, var list : MutableList<MultiItemEntity>, commonModelBrId : Int, bindId : Int)  : BaseMultiVMAdapter<MultiItemEntity>(commonModel,commonModelBrId,bindId) {

    init {
        this.setNewInstance(list = list)
    }
}


