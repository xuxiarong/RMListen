package debug

import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.databinding.DemoItemSingleNoClickBinding
import debug.model.SingleVmClickModel
import debug.viewmodel.DemoSingClickViewModel
import kotlinx.android.synthetic.main.activity_demo_single_click_vm.*

class DemoSingleClickVmActivity :
    BaseVMActivity<DemoItemSingleNoClickBinding, DemoSingClickViewModel>() {

    private val mAdapter by lazy {
        CommonBindVMAdapter(mViewModel, mutableListOf<SingleVmClickModel>(),R.layout.demo_item_single_vm_click,BR.viewModel,BR.singleClickModel)
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_demo_single_click_vm

    override fun startObserve() {

        mViewModel.singClickModelList.observe(this, Observer { list ->
            mAdapter.setList(list)
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun initData() {
        mViewModel.getSingClickModel()
    }

    override fun initView() {
        super.initView()
        demoSingleClickRv.bindVerticalLayout(mAdapter)
        mViewModel.demoItemClick = {singleItemClick(it)}
        mViewModel.demoNameClick = {singleNameClick(it)}
        demoSingleClickRvBtn.setOnClickListener {
            mViewModel.changeData()
        }
    }

    fun singleItemClick(model : SingleVmClickModel){
        DLog.d("suolong","singleItemClick = ${model.name} --- age = ${model.age}")
    }

    fun singleNameClick(model : SingleVmClickModel){
        DLog.d("suolong","singleNameClick = ${model.name} --- age = ${model.age}")
    }

    fun singleAgeClick(model : SingleVmClickModel){
        DLog.d("suolong","singleAgeClick = ${model.name} --- age = ${model.age}")
    }

}