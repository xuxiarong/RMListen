package com.rm.module_home.adapter

import com.rm.baselisten.adapter.BaseMultiAdapter

/**
 * desc   : 通用的多个Item的Adapter
 * date   : 2020/08/24
 * version: 1.0
 */
class CommonMultiAdapter (list : List<IBindItemType>, bindId : Int)  : BaseMultiAdapter<BaseMultiAdapter.IBindItemType>(list,bindId)
