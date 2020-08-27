package com.rm.baselisten.adapter

import com.rm.baselisten.mvvm.BaseViewModel

/**
 * desc   : 通用的多个Item的Adapter
 * date   : 2020/08/24
 * version: 1.0
 */
class CommonMultiVMAdapter (commonModel : BaseViewModel, list : List<IBindItemType>, commonModelBrId : Int, bindId : Int)  : BaseMultiVMAdapter<BaseMultiAdapter.IBindItemType>(commonModel,list,commonModelBrId,bindId) {

}
