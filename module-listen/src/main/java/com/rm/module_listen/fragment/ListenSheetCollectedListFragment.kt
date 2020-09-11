package com.rm.module_listen.fragment

import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetCollectedListAdapter
import com.rm.module_listen.databinding.ListenFragmentSheetCollectedListBinding
import com.rm.module_listen.viewmodel.ListenSheetCollectedListViewModel
import kotlinx.android.synthetic.main.listen_fragment_sheet_collected_list.*

class ListenSheetCollectedListFragment :
    BaseVMFragment<ListenFragmentSheetCollectedListBinding, ListenSheetCollectedListViewModel>() {

    companion object {
        fun newInstance(): ListenSheetCollectedListFragment {
            return ListenSheetCollectedListFragment()
        }
    }

    private val mAdapter by lazy {
        ListenSheetCollectedListAdapter(
            mViewModel,
            mutableListOf(),
            BR.viewModel,
            BR.item
        )
    }



    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it.list)
        }
    }

    override fun initLayoutId(): Int {
        return R.layout.listen_fragment_sheet_collected_list
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun initView() {
        super.initView()
        listen_sheet_collected_list_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearBottomItemDecoration(dimen(R.dimen.dp_18))
        }
    }
}