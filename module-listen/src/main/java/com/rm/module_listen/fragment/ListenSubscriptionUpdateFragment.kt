package com.rm.module_listen.fragment

import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenActivitySubscriptionBinding
import com.rm.module_listen.viewmodel.ListenMyListenViewModel

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class ListenSubscriptionUpdateFragment :
    BaseVMFragment<ListenActivitySubscriptionBinding, ListenMyListenViewModel>() {
    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }

    override fun initLayoutId() = R.layout.listen_fragment_subscription_update

    override fun initData() {

    }

    companion object {
        fun newInstance(): ListenSubscriptionUpdateFragment {
            return ListenSubscriptionUpdateFragment()
        }
    }
}