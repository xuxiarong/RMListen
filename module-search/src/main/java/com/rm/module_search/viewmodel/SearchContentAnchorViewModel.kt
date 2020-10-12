package com.rm.module_search.viewmodel

import android.view.View
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_search.*
import com.rm.module_search.bean.MemberBean
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.repository.SearchRepository

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchContentAnchorViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    val keyword = searchKeyword

    //主播adapter
    val anchorAdapter by lazy {
        CommonBindVMAdapter<MemberBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_anchor,
            BR.viewModel,
            BR.item
        )
    }


    val refreshStateMode = SmartRefreshLayoutStatusModel()

    //页码
    private var mPage = 1

    //每页展示数量
    private var mPageSize = 10


    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        requestData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++mPage
        requestData()
    }

    /**
     * 请求数据
     */
    private fun requestData() {
        launchOnIO {
            repository.searchResult(searchKeyword.get()!!, REQUEST_TYPE_MEMBER, mPage, mPageSize)
                .checkResult(
                    onSuccess = {
                        successData(it)
                    },
                    onError = {
                        failData()
                    }
                )
        }
    }

    /**
     * 成功数据
     */
    private fun successData(bean: SearchResultBean) {
        if (mPage == 1) {
            refreshStateMode.finishRefresh(true)
        } else {
            refreshStateMode.finishLoadMore(true)
        }
        refreshStateMode.setHasMore(bean.member_list.isNotEmpty())

        if (mPage == 1) {
            if (bean.member_list.isEmpty()) {
                showDataEmpty()
            } else {
                anchorAdapter.setList(bean.member_list)
            }
        } else {
            bean.member_list.let { anchorAdapter.addData(it) }
        }
    }

    /**
     * 失败数据
     */
    private fun failData() {
        if (mPage == 1) {
            refreshStateMode.finishRefresh(false)
        } else {
            refreshStateMode.finishLoadMore(false)
        }
    }

    /**
     *   item点击事件
     */
    fun itemClickFun(view: View, bean: MemberBean) {
    }
}