package com.rm.module_search.fragment

import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.lifecycle.observe
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.databinding.SearchFragmentContentBookBinding
import com.rm.module_search.searchResultData
import com.rm.module_search.viewmodel.SearchContentBooksViewModel

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description 书籍
 *
 */
class SearchContentBooksFragment :
    BaseVMFragment<SearchFragmentContentBookBinding, SearchContentBooksViewModel>() {

    override fun initLayoutId() = R.layout.search_fragment_content_book

    override fun initModelBrId() = BR.viewModel

    override fun initData() {
    }

    override fun startObserve() {
        searchResultData.observe(this) {
            val list = it.audio_list
            if (list.isNullOrEmpty()) {
                mViewModel.showSearchDataEmpty()
            } else {
                mViewModel.mPage = 1
                mViewModel.showContentView()
                mViewModel.bookAdapter.setList(list)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.bookAdapter.data.isEmpty()) {
            mViewModel.showSearchDataEmpty()
        }
    }

}