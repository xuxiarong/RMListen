package com.rm.baselisten.adapter.multi

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * desc   : 通用的纯展示的多Item的Adapter
 * date   : 2020/08/24
 * version: 1.0
 */
class CommonMultiAdapter<T : MultiItemEntity> (var dataList : MutableList<T>,bindId : Int)  : BaseMultiAdapter<T>(bindId) {
    override fun convert(holder: BaseViewHolder, item: T) {
    }

    init {
        setNewInstance(dataList)
    }
}
