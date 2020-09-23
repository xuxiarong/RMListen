package debug.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_main.R

/**
 * desc   :
 * date   : 2020/09/23
 * version: 1.0
 */
data class DemoTopView constructor(val content :String = "") : MultiItemEntity{
    override val itemType = R.layout.demo_item_top_view
}