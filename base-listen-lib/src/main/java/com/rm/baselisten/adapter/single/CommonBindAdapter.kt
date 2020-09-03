package com.rm.baselisten.adapter.single

/**
 * desc   : 通用的纯展示的单Item的Adapter
 * date   : 2020/08/27
 * version: 1.0
 */
open class CommonBindAdapter<T> constructor(commonData : MutableList<T>,commonItemId : Int,commonBrId : Int) : BaseBindAdapter<T>(commonData,commonItemId,commonBrId)



