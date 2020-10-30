package debug

import android.content.Intent
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.databinding.HomeActivityMainBinding
import kotlinx.android.synthetic.main.home_activity_main.*


/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class HomeMainDebugActivity : BaseVMActivity<HomeActivityMainBinding, BaseVMViewModel>() {

    override fun initView() {
//
        btnDetail.setOnClickListener {
            startActivity(Intent(this, HomeDetailActivity::class.java))
        }
    }

    override fun initData() {
    }

    override fun getLayoutId() = R.layout.home_activity_main

    override fun startObserve() {

    }

    override fun initModelBrId() = BR.viewModel
}