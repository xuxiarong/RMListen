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

    private val footView by lazy {
        LayoutInflater.from(context).inflate(R.layout.business_foot_view, null)
    }
    override fun initLayoutId() = R.layout.search_fragment_content_book

    override fun initModelBrId() = BR.viewModel

    override fun initData() {
        mViewModel.loadErrorBlock = {
            shtTip(it)
        }
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

        mViewModel.refreshStateMode.noMoreData.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStateMode.noMoreData.get()
                if (hasMore == true) {
                    mViewModel.bookAdapter.removeAllFooterView()
                    mViewModel.bookAdapter.addFooterView(footView)
                } else {
                    mViewModel.bookAdapter.removeAllFooterView()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.bookAdapter.data.isEmpty()) {
            mViewModel.showSearchDataEmpty()
        }
    }

}