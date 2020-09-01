package debug

import com.rm.baselisten.adapter.multi.CommonMultiAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_main.BR
import com.rm.module_main.R
import debug.model.DemoMultiModel1
import debug.model.DemoMultiModel2
import kotlinx.android.synthetic.main.activity_demo_multi_rv.*

class DemoMultiRvActivity : BaseActivity() {


    private val mAdapter by lazy {
        CommonMultiAdapter(mMultiData,BR.item)
    }

    var mMultiData  = mutableListOf(
        DemoMultiModel2("精品推荐", "更多"),
        DemoMultiModel1("张三", 18),
        DemoMultiModel1("李四", 19),
        DemoMultiModel2("发现更多", "阅读"),
        DemoMultiModel1("王五", 22),
        DemoMultiModel1("赵六", 33)
    )

    override fun initData() {
    }

    override fun getLayoutId() = R.layout.activity_demo_multi_rv

    override fun initView() {
        demoMultiNoClickRv.bindVerticalLayout(mAdapter)
    }

}