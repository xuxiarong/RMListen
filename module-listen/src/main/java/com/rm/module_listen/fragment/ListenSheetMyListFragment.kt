package com.rm.module_listen.fragment

import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.business_lib.bean.SheetBean
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenFragmentSheetMyListBinding
import com.rm.module_listen.viewmodel.ListenSheetMyListViewModel
import kotlinx.android.synthetic.main.listen_fragment_sheet_my_list.*

class ListenSheetMyListFragment :
    BaseVMFragment<ListenFragmentSheetMyListBinding, ListenSheetMyListViewModel>() {

    companion object {
        fun newInstance(): ListenSheetMyListFragment {
            return ListenSheetMyListFragment()
        }
    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<SheetBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_sheet_my_list,
            BR.click,
            BR.item
        )
    }

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        super.initView()
        listen_sheet_my_list_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearBottomItemDecoration(dimen(R.dimen.dp_14))
        }
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it)
        }
    }

    override fun initLayoutId(): Int {
        return R.layout.listen_fragment_sheet_my_list
    }

    override fun initData() {
        mViewModel.getData()
    }
}