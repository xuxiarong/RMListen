package com.rm.module_search.activity

import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayoutMediator
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.adapter.SearchResultAdapter
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ALL
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ANCHOR
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_BOOKS
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_SHEET
import com.rm.module_search.databinding.SearchActivityResultBinding
import com.rm.module_search.searchKeyword
import com.rm.module_search.viewmodel.SearchResultViewModel

/**
 *
 * @author yuanfang
 * @date 10/10/20
 * @description
 *
 */
class SearchResultActivity : BaseVMActivity<SearchActivityResultBinding, SearchResultViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.search_activity_result

    override fun initView() {
        super.initView()
        mDataBind.searchResultEditText.setText(searchKeyword.get()!!)
        mDataBind.searchResultViewPager.adapter = SearchResultAdapter(this, mViewModel.tabList)
        attachViewPager()
    }

    override fun initData() {
        mViewModel.searchResult(searchKeyword.get()!!)
    }

    override fun startObserve() {
    }

    /**
     * tabLayout绑定viewpager滑动事件
     */
    private fun attachViewPager() {
        BendTabLayoutMediator(mDataBind.searchResultTabLayout, mDataBind.searchResultViewPager,
            BendTabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = when (mViewModel.tabList[position]) {
                    TYPE_CONTENT_ALL -> {
                        getString(R.string.search_all)
                    }
                    TYPE_CONTENT_BOOKS -> {
                        getString(R.string.search_books)
                    }
                    TYPE_CONTENT_ANCHOR -> {
                        getString(R.string.search_anchor)
                    }
                    TYPE_CONTENT_SHEET -> {
                        getString(R.string.search_sheet)
                    }
                    else -> {
                        getString(R.string.search_all)
                    }
                }
            }).attach()

    }
}