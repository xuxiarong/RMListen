package com.rm.module_search.fragment

import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.databinding.SearchFragmentContentBinding
import com.rm.module_search.viewmodel.SearchContentAllViewModel
import kotlinx.android.synthetic.main.search_fragment_content.*

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description 书籍
 *
 */
class SearchContentBooksFragment :
    BaseVMFragment<SearchFragmentContentBinding, SearchContentAllViewModel>() {

    override fun initLayoutId() = R.layout.search_fragment_content

    override fun initModelBrId() = BR.viewModel
    override fun initView() {
        super.initView()
        search_adapter_content_rv.apply {
            bindVerticalLayout(mViewModel.bookAdapter)
        }

    }
    override fun initData() {

    }

    override fun startObserve() {
    }

}