package com.rm.module_listen.fragment

import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_DELETE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_EDIT
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_REQUEST_CODE
import com.rm.module_listen.bean.ListenSheetBean
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
        CommonBindVMAdapter<ListenSheetBean>(
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
        mViewModel.itemClick = { startDetail(it) }
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it.list)
        }
    }

    override fun initLayoutId(): Int {
        return R.layout.listen_fragment_sheet_my_list
    }

    override fun initData() {
        mViewModel.getData()
    }

    private fun startDetail(bean: ListenSheetBean) {
        activity?.let {
            ListenMySheetDetailActivity.startActivity(it, bean)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LISTEN_SHEET_DETAIL_REQUEST_CODE) {
            when (resultCode) {
                //删除
                LISTEN_SHEET_DETAIL_DELETE,LISTEN_SHEET_DETAIL_EDIT -> {
                    mViewModel.getData()
                }
                else -> {
                }
            }
        }
    }
}