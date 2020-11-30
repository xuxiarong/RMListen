package com.rm.module_listen.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_DELETE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_REQUEST_CODE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_AUDIO_NUM
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_ID
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_NAME
import com.rm.module_listen.activity.ListenSheetListActivity.Companion.MEMBER_ID
import com.rm.module_listen.databinding.ListenFragmentSheetMyListBinding
import com.rm.module_listen.viewmodel.ListenSheetMyListViewModel

class ListenSheetMyListFragment :
    BaseVMFragment<ListenFragmentSheetMyListBinding, ListenSheetMyListViewModel>() {

    companion object {
        fun newInstance(memberId: String): ListenSheetMyListFragment {
            val fragment = ListenSheetMyListFragment()
            val bundle = Bundle()
            bundle.putString(MEMBER_ID, memberId)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val footView by lazy {
        LayoutInflater.from(context).inflate(R.layout.business_foot_view, null)
    }


    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.listen_fragment_sheet_my_list

    override fun initData() {
        mViewModel.showLoading()
        arguments?.getString(MEMBER_ID)?.let {
            mViewModel.memberId = it
        }
        mViewModel.getData(mViewModel.memberId)
    }

    override fun startObserve() {
        mViewModel.refreshStateModel.noMoreData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStateModel.noMoreData.get()
                if (hasMore == true) {
                    mViewModel.mAdapter.removeAllFooterView()
                    mViewModel.mAdapter.addFooterView(footView)
                } else {
                    mViewModel.mAdapter.removeAllFooterView()
                }
            }
        })
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

                LISTEN_SHEET_DETAIL -> {
                    val sheetName = data?.getStringExtra(SHEET_NAME) ?: ""
                    val sheetAudioNum = data?.getIntExtra(SHEET_AUDIO_NUM, 0) ?: 0
                    mViewModel.changeData(sheetId, sheetName, sheetAudioNum)
                }

            }
        }
    }
}