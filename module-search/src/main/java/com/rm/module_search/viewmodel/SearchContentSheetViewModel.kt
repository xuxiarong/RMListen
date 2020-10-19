package com.rm.module_search.viewmodel

import android.view.View
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_search.*
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.bean.SearchSheetBean
import com.rm.module_search.repository.SearchRepository

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchContentSheetViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    val keyword = searchKeyword

    //听单adapter
    val sheetAdapter by lazy {
        CommonBindVMAdapter<SearchSheetBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_sheet,
            BR.viewModel,
            BR.item
        )
    }

    val refreshStateMode = SmartRefreshLayoutStatusModel()

    //页码
    private var mPage = 1

    //每页展示数量
    private var mPageSize = 12

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
            repository.searchResult(searchKeyword.get()!!, REQUEST_TYPE_SHEET, mPage, mPageSize)
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

        refreshStateMode.setHasMore(bean.sheet_list.isNotEmpty())

        if (mPage == 1) {
            if (bean.sheet_list.isEmpty()) {
                showDataEmpty()
            } else {
                sheetAdapter.setList(bean.sheet_list)
            }
        } else {
            bean.sheet_list.let { sheetAdapter.addData(it) }
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
     * item点击事件
     */
    fun itemClickFun(view: View, bean: SearchSheetBean) {
        getActivity(view.context)?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(it, bean.sheet_id, 0)
        }
    }
}