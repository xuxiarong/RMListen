package com.rm.module_search.viewmodel

import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_search.*
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.repository.SearchRepository

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchContentBooksViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    //书籍adapter
    val bookAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_books,
            BR.viewModel,
            BR.item
        ).apply {
            setList(searchResultData.get()?.audio_list)
        }
    }

    val refreshStateMode = SmartRefreshLayoutStatusModel()

    //页码
    private var mPage = 1

    //
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
        refreshStateMode.setHasMore(bean.audio_list?.size ?: 0 >= 0)
        if (mPage == 1) {
            bookAdapter.setList(bean.audio_list)
        } else {
            bean.audio_list?.let { bookAdapter.addData(it) }
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

}