package com.rm.module_listen.fragment

import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
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

        mViewModel.itemClick = {
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(activity!!, it.sheet_id.toString(),100)
        }
    }

    /**
     * 跳转activity回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 200) {
            val isFavorite = data?.getBooleanExtra("isFavorite", true)
            val sheetId = data?.getStringExtra("sheetId")
            if (isFavorite == false) {
                changeAdapter(sheetId)
            }
        }
    }

    /**
     * 如果当前的听单取消收藏是，将当前听单移除
     */
    private fun changeAdapter(sheetId: String?) {
        val bean = mViewModel.data.value
        bean?.let {
            val index = it.getIndex(sheetId)
            if (index != -1) {
                mAdapter.removeAt(index)
            }
        }
    }
}