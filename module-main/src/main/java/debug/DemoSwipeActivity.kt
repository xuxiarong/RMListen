package debug

import android.os.Bundle
import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.databinding.DemoSwipeCloseBinding
import debug.viewmodel.DemoSwipeViewModel
import kotlinx.android.synthetic.main.demo_swipe_close.*

class DemoSwipeActivity : BaseVMActivity<DemoSwipeCloseBinding,DemoSwipeViewModel>() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rl_swipe.setOnClickListener {
            ToastUtil.show(this@DemoSwipeActivity,"我是点击事件")
        }

        rl_swipe.setOnLongClickListener {
            ToastUtil.show(this@DemoSwipeActivity,"我是长按事件")
            true
        }
        to_top.setOnClickListener {
            ToastUtil.show(this@DemoSwipeActivity,"我是置顶点击事件")
            sw.close()
        }

        mViewModel.mSwipeAdapter.setHasStableIds(true)
        mDataBind.demoSwipeRv.bindVerticalLayout(mViewModel.mSwipeAdapter)

    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.swipeData.observe(this, Observer {
            mViewModel.mSwipeAdapter.setList(it)
        })
    }

    override fun initData() {
        mViewModel.testSwipeData()
    }

    override fun getLayoutId() = R.layout.demo_swipe_close


}