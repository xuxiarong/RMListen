package debug

import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.databinding.ActivityDemoMultiClickVmBinding
import debug.model.DemoMultiModel1
import debug.model.DemoMultiModel2
import debug.viewmodel.DemoMultiClickViewModel

class DemoMultiClickVmActivity : BaseVMActivity<ActivityDemoMultiClickVmBinding, DemoMultiClickViewModel>() {

    private val mAdapter by lazy {
        CommonMultiVMAdapter(mViewModel, mutableListOf(),
            BR.viewModel,
            BR.item)
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_demo_multi_click_vm

    override fun startObserve() {

        mViewModel.multiList.observe(this, Observer { list ->
            mAdapter.setNewInstance(list)
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun initData() {
        mViewModel.getMultiData()
    }

    override fun initView() {
        super.initView()
        mViewModel.demoItem1Click = {demoItem1Click(it)}
        mViewModel.demoItem2Click = {demoItem2Click(it)}
        mDataBind?.demoMultiClickRv?.bindVerticalLayout(mAdapter)
        mDataBind?.demoMultiClickRvBtn?.setOnClickListener {
            mViewModel.getServiceData()
        }
    }

    fun demoItem2Click(item2 : DemoMultiModel2){
        DLog.d("suolong", "item2Click: model = ${item2.title}" )
    }

    fun demoItem1Click(item1 : DemoMultiModel1){
        DLog.d("suolong", "item2Click: model = ${item1.name}" )
    }

}