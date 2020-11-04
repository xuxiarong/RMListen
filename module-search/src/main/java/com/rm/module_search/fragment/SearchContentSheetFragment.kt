package com.rm.module_search.fragment

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
        mViewModel.loadErrorBlock = {
            shtTip(it)
        }
    }


    override fun startObserve() {
        searchResultData.observe(this) {
            val list = it.sheet_list
            if (list.isNullOrEmpty()) {
                mViewModel.showDataEmpty()
            } else {
                mViewModel.mPage = 1
                mViewModel.showContentView()
                mViewModel.sheetAdapter.setList(list)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.sheetAdapter.data.isEmpty()) {
            mViewModel.showDataEmpty()
        }
    }

}