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

    private val footView by lazy {
        LayoutInflater.from(context).inflate(R.layout.business_foot_view, null)

    }
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
                mViewModel.showSearchDataEmpty()
            } else {
                mViewModel.mPage = 1
                mViewModel.showContentView()
                mViewModel.anchorAdapter.setList(list)
            }
        }

        mViewModel.refreshStateMode.isHasMore.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStateMode.isHasMore.get()
                if (hasMore == true) {
                    mViewModel.anchorAdapter.removeAllFooterView()
                    mViewModel.anchorAdapter.addFooterView(footView)
                } else {
                    mViewModel.anchorAdapter.removeAllFooterView()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.anchorAdapter.data.isEmpty()) {
            mViewModel.showSearchDataEmpty()
        }
    }

}