package com.rm.module_listen.fragment

import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenFragmentRecentListenBinding
import com.rm.module_listen.viewmodel.ListenRecentListenViewModel

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class ListenRecentListenFragment: BaseVMFragment<ListenFragmentRecentListenBinding, ListenRecentListenViewModel>() {


    override fun initLayoutId() = R.layout.listen_fragment_recent_listen
    override fun initModelBrId() = BR.viewModel



    override fun startObserve() {
        mViewModel.allHistory.observe(this, Observer {
            mViewModel.mSwipeAdapter.setList(it)
        })
    }


    override fun initView() {
        super.initView()
        mDataBind.listenRecentRv.bindVerticalLayout(mViewModel.mSwipeAdapter)
    }


    override fun onStart() {
        super.onStart()
//        mViewModel.getListenHistory()
    }

    override fun initData() {
    }

    companion object {
        fun newInstance(): ListenRecentListenFragment {
            return ListenRecentListenFragment()
        }
    }


}