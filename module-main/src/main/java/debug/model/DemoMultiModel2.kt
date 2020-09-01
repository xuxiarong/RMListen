package debug.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_main.R

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */

data class DemoMultiModel2 constructor(val title: String, val rightText: String) :
    MultiItemEntity {
    override val itemType =
        R.layout.demo_item_multi_no_click_type2

}