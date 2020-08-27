package debug

import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_main.R
import com.rm.module_main.databinding.MainActivityDataBindVmDemoBinding
import debug.viewmodel.DemoViewModel

class DataBindVmDemoActivity : BaseVMActivity<MainActivityDataBindVmDemoBinding, DemoViewModel>() {


    override fun initView() {
        setStatusBar(R.color.base_activity_bg_color)
    }

    override fun initData() {

    }

    override fun startObserve() {

    }

    override fun getLayoutId() = R.layout.main_activity_data_bind_vm_demo

}