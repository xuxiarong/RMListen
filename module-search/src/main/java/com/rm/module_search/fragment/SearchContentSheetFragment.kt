package com.rm.module_search.fragment

import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.lifecycle.observe
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.databinding.SearchFragmentContentSheetBinding
import com.rm.module_search.searchResultData
import com.rm.module_search.viewmodel.SearchContentSheetViewModel

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description 听单
 *
 */
class SearchContentSheetFragment :
    BaseVMFragment<SearchFragmentContentSheetBinding, SearchContentSheetViewModel>() {

    override fun initLayoutId() = R.layout.search_fragment_content_sheet

    override fun initModelBrId() = BR.viewModel

    override fun initData() {
    }


    override fun startObserve() {
        searchResultData.observe(this) {
            mViewModel.mPage = 1
            mViewModel.refreshStateMode.setResetNoMoreData(true)
            mViewModel.successData(it)
        }

    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.sheetAdapter.data.isEmpty()) {
            mViewModel.showSearchDataEmpty()
        }
    }

}