package com.rm.module_search.viewmodel

import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.bean.MemberBean
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.bean.SearchSheetBean
import com.rm.module_search.keyword
import com.rm.module_search.repository.SearchRepository
import com.rm.module_search.searchResultData

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchContentAllViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
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

    //主播adapter
    val anchorAdapter by lazy {
        CommonBindVMAdapter<MemberBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_anchor,
            BR.viewModel,
            BR.item
        ).apply {
            setList(searchResultData.get()?.member_list)
        }
    }

    //听单adapter
    val sheetAdapter by lazy {
        CommonBindVMAdapter<SearchSheetBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_sheet,
            BR.viewModel,
            BR.item
        ).apply {
            setList(searchResultData.get()?.sheet_list)
        }
    }

    val refreshStateMode = SmartRefreshLayoutStatusModel()

    //页码
    private var mPage = 1

    //
    //每页展示数量
    private var mPageSize = 10

    //当前请求的类型
    var requestType = REQUEST_TYPE_ALL

    companion object {
        // all：全部；member：主播；audio：书籍；sheet：书单
        const val REQUEST_TYPE_ALL = "all"
        const val REQUEST_TYPE_MEMBER = "member"
        const val REQUEST_TYPE_AUDIO = "audio"
        const val REQUEST_TYPE_SHEET = "sheet"
    }

    fun clickBookFun() {

    }

    fun clickAnchorFun() {

    }

    fun clickSheetFun() {

    }

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
            repository.searchResult(keyword.get()!!, requestType, mPage, mPageSize).checkResult(
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
        processData(bean)
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
     * 处理数据
     */
    private fun processData(bean: SearchResultBean) {
        when (requestType) {
            REQUEST_TYPE_ALL -> {
                refreshStateMode.setHasMore(false)
                searchResultData.set(bean)
            }
            REQUEST_TYPE_MEMBER -> {
                processAudioData(bean.member_list)
            }
            REQUEST_TYPE_AUDIO -> {
                processMemberData(bean.audio_list)
            }
            REQUEST_TYPE_SHEET -> {
                processSheetData(bean.sheet_list)
            }
        }
    }

    /**
     * 处理书籍数据
     */
    private fun processAudioData(memberList: List<MemberBean>) {
        refreshStateMode.setHasMore(memberList.size >= 0)
        if (mPage==1){
//            bookAdapter.setList(me)
        }
    }

    /**
     * 处理主播数据
     */
    private fun processMemberData(audioList: List<AudioBean>) {
        refreshStateMode.setHasMore(audioList.size >= 0)
    }

    /**
     * 处理听单数据
     */
    private fun processSheetData(sheetList: List<SearchSheetBean>) {


        refreshStateMode.setHasMore(sheetList.size >= 0)
    }
}