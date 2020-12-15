package com.rm.module_search.fragment

import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.lifecycle.observe
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.databinding.SearchFragmentContentAnchorBinding
import com.rm.module_search.searchResultData
import com.rm.module_search.viewmodel.SearchContentAnchorViewModel

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description 主播列表
 *
 */
class SearchContentAnchorFragment :
    BaseVMFragment<SearchFragmentContentAnchorBinding, SearchContentAnchorViewModel>() {
    override fun initLayoutId() = R.layout.search_fragment_content_anchor

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
        if (mViewModel.anchorAdapter.data.isEmpty()) {
            mViewModel.showSearchDataEmpty()
        }
    }

}