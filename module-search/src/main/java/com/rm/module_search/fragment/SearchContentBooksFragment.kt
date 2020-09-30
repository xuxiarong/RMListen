package com.rm.module_search.fragment

import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.databinding.SearchFragmentContentBookBinding
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
    }

}