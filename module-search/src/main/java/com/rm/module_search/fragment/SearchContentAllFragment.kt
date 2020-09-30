package com.rm.module_search.fragment

import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.databinding.SearchFragmentContentAllBinding
import com.rm.module_search.viewmodel.SearchContentAllViewModel

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description 全部
 *
 */
class SearchContentAllFragment :
    BaseVMFragment<SearchFragmentContentAllBinding, SearchContentAllViewModel>() {


    override fun initLayoutId() = R.layout.search_fragment_content_all

    override fun initModelBrId() = BR.viewModel

    override fun initData() {

    }

    override fun startObserve() {
    }

}