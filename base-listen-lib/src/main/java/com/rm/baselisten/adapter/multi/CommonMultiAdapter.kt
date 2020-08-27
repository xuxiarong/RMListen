package com.rm.baselisten.adapter.multi

import com.rm.baselisten.holder.BaseBindHolder

/**
 * desc   : 通用的多个Item的Adapter
 * date   : 2020/08/24
 * version: 1.0
 */
class CommonMultiAdapter (list : List<IBindItemType>, bindId : Int)  : BaseMultiAdapter<BaseMultiAdapter.IBindItemType>(list,bindId) {

    override fun convert(holder: BaseBindHolder, position: Int) {

    }
}
