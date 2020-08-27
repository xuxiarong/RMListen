package debug

import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.databinding.DemoActivityDataBindVmDemoBinding
import debug.viewmodel.DemoViewModel

class DataBindVmDemoActivity : BaseVMActivity<DemoActivityDataBindVmDemoBinding, DemoViewModel>() {


    override fun initView() {
        setStatusBar(R.color.base_activity_bg_color)

        mViewModel.getDemoData()

    }

    override fun initData() {

    }

    override fun startObserve() {
//        mViewModel.demoData.observe(this, Observer {
//            mDataBind.demoRv.bindVerticalLayout(
//                CommonBindAdapter(
//                    it, ,
//                )
//            )
//
//        })
    }

    override fun getLayoutId() = R.layout.demo_activity_data_bind_vm_demo
    override fun initModelBrId() = BR.viewModel
}

