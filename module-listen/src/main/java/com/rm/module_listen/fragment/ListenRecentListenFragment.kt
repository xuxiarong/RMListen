package com.rm.module_listen.fragment

import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenFragmentRecentListenBinding
import com.rm.module_listen.viewmodel.ListenMyListenViewModel

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class ListenRecentListenFragment: BaseVMFragment<ListenFragmentRecentListenBinding, ListenMyListenViewModel>() {

    private val mRecentAdapter : CommonMultiVMAdapter by lazy {
        CommonMultiVMAdapter(mViewModel, mutableListOf(),BR.viewModel,BR.item)
    }
    override fun initLayoutId() = R.layout.listen_fragment_recent_listen
    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.todayListenData.observe(this, Observer {
            mRecentAdapter.addData(it)
        })
        mViewModel.lastMonthListenData.observe(this, Observer {
            mRecentAdapter.addData(it)
        })
        mViewModel.earlyListenData.observe(this, Observer {
            mRecentAdapter.addData(it)
        })
    }


    override fun initView() {
        super.initView()
        mDataBind.listenRecentRv.bindVerticalLayout(mRecentAdapter)
    }


    override fun initData() {
        mViewModel.getTodayListenDataFromLocal()
        mViewModel.getRecentMonthListenDataFromLocal()
        mViewModel.getEarlyListenDataFromLocal()
    }


    companion object {
        fun newInstance(): ListenRecentListenFragment {
            return ListenRecentListenFragment()
        }
    }


}