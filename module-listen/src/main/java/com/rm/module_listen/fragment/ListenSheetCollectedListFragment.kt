package com.rm.module_listen.fragment

import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.dimen
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetCollectedListAdapter
import com.rm.module_listen.databinding.ListenFragmentSheetCollectedListBinding
import com.rm.module_listen.viewmodel.ListenSheetCollectedListViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.listen_activity_subscription.*
import kotlinx.android.synthetic.main.listen_fragment_sheet_collected_list.*

class ListenSheetCollectedListFragment :
    BaseVMFragment<ListenFragmentSheetCollectedListBinding, ListenSheetCollectedListViewModel>() {

    companion object {
        fun newInstance(): ListenSheetCollectedListFragment {
            return ListenSheetCollectedListFragment()
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.listen_fragment_sheet_collected_list

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getData()
    }

    override fun startObserve() {
    }

    /**
     * 跳转activity回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 200) {
            val isFavorite = data?.getBooleanExtra("isFavorite", true)
            val sheetId = data?.getStringExtra("sheetId") ?: ""
            if (isFavorite == false) {
                mViewModel.remove(sheetId)
            }
        }
    }


}