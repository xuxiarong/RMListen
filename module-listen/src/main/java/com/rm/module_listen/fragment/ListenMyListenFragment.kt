package com.rm.module_listen.fragment

import com.rm.baselisten.databinding.ActivityBaseVmBinding
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.viewmodel.ListenMyListenViewModel

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenMyListenFragment : BaseVMFragment<ActivityBaseVmBinding,ListenMyListenViewModel>() {
    override fun initModelBrId()= BR.viewModel
    override fun initLayoutId()= R.layout.listen_fragment_my_listen

    override fun startObserve() {

    }

    override fun initData() {
    }
}