package debug

import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.databinding.DemoActivityDataBindVmDemoBinding
import debug.viewmodel.DemoViewModel

class DemoDataBindVmActivity : BaseVMActivity<DemoActivityDataBindVmDemoBinding, DemoViewModel>() {


    override fun initView() {
        setStatusBar(R.color.base_activity_bg_color)
        mViewModel.getDemoData()
    }

    override fun initData() {
        setStatusBar(R.color.base_activity_bg_color)

    }

    override fun startObserve() {
        setStatusBar(R.color.base_activity_bg_color)
    }

    override fun getLayoutId() = R.layout.demo_activity_data_bind_vm_demo
    override fun initModelBrId() = BR.viewModel
}

