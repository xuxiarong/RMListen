package debug

import android.os.Bundle
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.databinding.DemoSwipeRvItemBinding
import debug.viewmodel.DemoSwipeViewModel
import kotlinx.android.synthetic.main.demo_swipe_close.*

class SwipeColseActivity : BaseVMActivity<DemoSwipeRvItemBinding,DemoSwipeViewModel>() {




//    private val mSwipeAdapter : CommonBindVMAdapter<DemoSingViewModel> by lazy {
//        CommonBindVMAdapter(mViewModel,mViewModel.canClick)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rl_swipe.setOnClickListener {
            ToastUtil.show(this@SwipeColseActivity,"我是点击事件")
        }

        rl_swipe.setOnLongClickListener {
            ToastUtil.show(this@SwipeColseActivity,"我是长按事件")
            true
        }
        to_top.setOnClickListener {
            ToastUtil.show(this@SwipeColseActivity,"我是置顶点击事件")
            sw.close()
        }

//        demoSwipeRv.bindVerticalLayout()


    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }

    override fun initData() {
    }

    override fun getLayoutId() = R.layout.demo_swipe_close


}