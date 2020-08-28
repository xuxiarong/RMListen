package debug.model

import com.rm.baselisten.adapter.multi.BaseMultiAdapter
import com.rm.module_main.R

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */

data class DemoMultiModel1 constructor(val name: String, val age: Int) :
    BaseMultiAdapter.IBindItemType {
    override fun bindType(): Int {
        return R.layout.demo_item_multi_no_click_type1
    }
}