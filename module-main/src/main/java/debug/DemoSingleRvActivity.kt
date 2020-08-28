package debug

import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.module_main.BR
import com.rm.module_main.R
import debug.model.SingleDemoModel
import kotlinx.android.synthetic.main.activity_demo_single_rv.*

class DemoSingleRvActivity : BaseActivity() {

    override fun initData() {

    }

    override fun getLayoutId() = R.layout.activity_demo_single_rv

    override fun initView() {
        super.initView()
        setStatusBar(R.color.base_activity_bg_color)
        val singData = listOf<SingleDemoModel>(
            SingleDemoModel("张三", 18),
            SingleDemoModel("李四", 19),
            SingleDemoModel("王五", 20)
        )
        demoSingleRv.bindVerticalLayout(CommonBindAdapter(singData,R.layout.demo_item_single_no_click,BR.singleModel))
    }

}