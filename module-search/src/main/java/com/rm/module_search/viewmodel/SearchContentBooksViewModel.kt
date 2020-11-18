package com.rm.module_search.viewmodel

import android.view.View
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.REQUEST_TYPE_AUDIO
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.repository.SearchRepository
import com.rm.module_search.searchKeyword

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchContentBooksViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    val keyword = searchKeyword

    //书籍adapter
    val bookAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_books,
            BR.viewModel,
            BR.item
        )
    }

    val refreshStateMode = SmartRefreshLayoutStatusModel()

    //页码
    var mPage = 1

    //每页展示数量
    private val mPageSize = 12

    //加载失败
    var loadErrorBlock: (String) -> Unit = {}


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
        ++mPage
        requestData()
    }

    /**
     * 请求数据
     */
    private fun requestData() {
        launchOnIO {
            repository.searchResult(searchKeyword.get()!!, REQUEST_TYPE_AUDIO, mPage, mPageSize)
                .checkResult(
                    onSuccess = {
                        successData(it)
                    },
                    onError = { msg ->
                        failData(msg)
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

        if (mPage == 1) {
            if (bean.audio_list.isEmpty()) {
                showSearchDataEmpty()
            } else {
                bookAdapter.setList(bean.audio_list)
            }
        } else {
            bean.audio_list.let { bookAdapter.addData(it) }
        }
        refreshStateMode.setNoHasMore(bean.audio_list.size < mPageSize || bookAdapter.data.size >= bean.audio)

    }

    /**
     * 失败数据
     */
    private fun failData(msg: String?) {
        if (mPage == 1) {
            refreshStateMode.finishRefresh(false)
        } else {
            mPage--
            refreshStateMode.finishLoadMore(false)
        }
        loadErrorBlock("$msg")
    }

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bean: DownloadAudio) {
        RouterHelper.createRouter(HomeService::class.java)
            .toDetailActivity(view.context, bean.audio_id.toString())
    }
}