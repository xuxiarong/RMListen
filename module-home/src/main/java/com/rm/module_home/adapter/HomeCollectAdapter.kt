package com.rm.module_home.adapter

import com.rm.baselisten.adapter.BaseBindAdapter
import com.rm.module_home.model.home.collect.HomeCollectModel

/**
 * desc   :
 * date   : 2020/08/21
 * version: 1.0
 */

class HomeCollectAdapter constructor(
    var collectList: List<HomeCollectModel>,
    var itemId: Int,
    var itemBrId: Int
) :
    BaseBindAdapter<HomeCollectModel>(collectList, itemId, itemBrId)

