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
    val contentRvId = R.id.search_adapter_content_rv

    //页码
    var mPage = 1

    //每页展示数量
    private val mPageSize = 12


    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStateMode.setNoHasMore(false)
        requestData()
    }

    /**
     * 加载更多
     */
    fun loadData() {
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
                    onError = {msg,_->
                        failData(msg)
                    }
                )
        }
    }

    /**
     * 成功数据
     */
    fun successData(bean: SearchResultBean) {
        showContentView()
        if (mPage == 1) {
            refreshStateMode.finishRefresh(true)
        } else {
            refreshStateMode.finishLoadMore(true)
        }

        if (mPage == 1) {
            if (bean.sheet_list.isEmpty()) {
                showSearchDataEmpty()
            } else {
                sheetAdapter.setList(bean.sheet_list)
            }
        } else {
            bean.sheet_list.let { sheetAdapter.addData(it) }
        }
        if (sheetAdapter.data.size >= bean.sheet) {
            refreshStateMode.setNoHasMore(true)
        } else {
            ++mPage
        }
    }

    /**
     * 失败数据
     */
    private fun failData(msg: String?) {
        if (mPage == 1) {
            refreshStateMode.finishRefresh(false)
        } else {
            refreshStateMode.finishLoadMore(false)
        }

        showTip("$msg", R.color.business_color_ff5e5e)
    }

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bean: SearchSheetBean) {
        getActivity(view.context)?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(it, bean.sheet_id)
        }
    }
}