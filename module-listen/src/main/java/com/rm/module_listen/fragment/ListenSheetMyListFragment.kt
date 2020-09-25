package com.rm.module_listen.fragment

import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_DELETE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_EDIT
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_REQUEST_CODE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_ID
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_NAME
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.databinding.ListenFragmentSheetMyListBinding
import com.rm.module_listen.viewmodel.ListenSheetMyListViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.listen_fragment_sheet_my_list.*

class ListenSheetMyListFragment :
    BaseVMFragment<ListenFragmentSheetMyListBinding, ListenSheetMyListViewModel>() {

    companion object {
        fun newInstance(): ListenSheetMyListFragment {
            return ListenSheetMyListFragment()
        }
    }


    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.listen_fragment_sheet_my_list

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getData()
    }

    override fun startObserve() {
    }

    /**
     * activity回调监听
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LISTEN_SHEET_DETAIL_REQUEST_CODE) {
            val sheetId = data?.getStringExtra(SHEET_ID) ?: ""
            when (resultCode) {
                //删除
                LISTEN_SHEET_DETAIL_DELETE -> {
                    mViewModel.remove(sheetId)
                }
                //编辑成功
                LISTEN_SHEET_DETAIL_EDIT -> {
                    val sheetName = data?.getStringExtra(SHEET_NAME) ?: ""
                    mViewModel.changeData(sheetId, sheetName)
                }
            }
        }
    }
}