package com.rm.module_listen.fragment

import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenFragmentSubscriptionUpdateBinding
import com.rm.module_listen.model.ListenSubsDateModel
import com.rm.module_listen.viewmodel.ListenSubsUpdateViewModel

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class ListenSubscriptionUpdateFragment :
    BaseVMFragment<ListenFragmentSubscriptionUpdateBinding, ListenSubsUpdateViewModel>() {

    private val mSubsDateAdapter : CommonBindVMAdapter<ListenSubsDateModel> by lazy {
        CommonBindVMAdapter(mViewModel, mutableListOf<ListenSubsDateModel>(),
            R.layout.listen_item_subs_date,
            BR.viewModel,
            BR.item)
    }

    private val mSubsAudioAdapter : CommonMultiVMAdapter by lazy {
        CommonMultiVMAdapter(
            mViewModel,
            mutableListOf(),
            BR.viewModel,
            BR.item
        )
    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.subsDateListDate.observe(this, Observer {
            mSubsDateAdapter.setList(it)
        })
    }

    override fun initLayoutId() = R.layout.listen_fragment_subscription_update

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getSubsDataFromService()
    }

    override fun initView() {
        super.initView()
        mDataBind.listenSubsDataRv.bindHorizontalLayout(mSubsDateAdapter)

    }

    companion object {
        fun newInstance(): ListenSubscriptionUpdateFragment {
            return ListenSubscriptionUpdateFragment()
        }
    }
}