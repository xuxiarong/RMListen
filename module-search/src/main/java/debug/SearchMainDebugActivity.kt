package debug

import com.rm.baselisten.debug.BaseDebugActivity
import com.rm.module_search.R
import kotlinx.android.synthetic.main.search_activity_result.*

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class SearchMainDebugActivity : BaseDebugActivity() {

    override fun getLayoutResId(): Int = R.layout.search_activity_result

    override fun initView() {
        search_result_back
    }

    override fun initData() {
        search_result_back
    }
}