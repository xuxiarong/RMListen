package com.rm.module_listen.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenSheetListActivity.Companion.MEMBER_ID
import com.rm.module_listen.databinding.ListenFragmentSheetCollectedListBinding
import com.rm.module_listen.viewmodel.ListenSheetCollectedListViewModel

class ListenSheetCollectedListFragment :
    BaseVMFragment<ListenFragmentSheetCollectedListBinding, ListenSheetCollectedListViewModel>() {

    companion object {
        fun newInstance(memberId: String): ListenSheetCollectedListFragment {
            val fragment = ListenSheetCollectedListFragment()
            val bundle = Bundle()
            bundle.putString(MEMBER_ID, memberId)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.listen_fragment_sheet_collected_list

    override fun initData() {
        mViewModel.showLoading()
        arguments?.getString(MEMBER_ID)?.let {
            mViewModel.memberId = it
            mViewModel.getFavorList(it)
        }

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
            if (TextUtils.isEmpty(mViewModel.memberId) && isFavorite == false) {
                mViewModel.remove(sheetId)
                if (mViewModel.mAdapter.data.size == 0) {
                    mViewModel.showDataEmpty()
                }
            }
        }
    }


}