package com.rm.module_search.fragment

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
        mViewModel.loadErrorBlock = {
            shtTip(it)
        }
    }

    override fun startObserve() {
        searchResultData.observe(this) {
            val list = it.member_list
            if (list.isNullOrEmpty()) {
                mViewModel.showDataEmpty()
            } else {
                mViewModel.mPage = 1
                mViewModel.showContentView()
                mViewModel.anchorAdapter.setList(list)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.anchorAdapter.data.isEmpty()) {
            mViewModel.showDataEmpty()
        }
    }

}