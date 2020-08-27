package com.rm.module_home.model.home.collect

import com.rm.baselisten.adapter.multi.BaseMultiAdapter
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeCollectRvModel : BaseMultiAdapter.IBindItemType {
    override fun bindType(): Int {
        return R.layout.home_item_collect_rv
    }
}