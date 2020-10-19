package com.rm.module_search.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_search.*
import com.rm.module_search.bean.MemberBean
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
class SearchContentAllViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    val keyword = searchKeyword

    val data =  ObservableField<SearchResultBean>()

    //书籍adapter
    val bookAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_books,
            BR.viewModel,
            BR.item
        )
    }

    //主播adapter
    val anchorAdapter by lazy {
        CommonBindVMAdapter<MemberBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_anchor,
            BR.viewModel,
            BR.item
        )
    }


    //听单adapter
    val sheetAdapter by lazy {
        CommonBindVMAdapter<SearchSheetBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_sheet,
            BR.viewModel,
            BR.item
        )
    }

    /**
     * 全部-书籍更多点击事件
     */
    fun clickBookMoreFun() {
        curType.postValue(REQUEST_TYPE_AUDIO)
    }

    /**
     * 全部-主播更多点击事件
     */
    fun clickAnchorMoreFun() {
        curType.postValue(REQUEST_TYPE_MEMBER)
    }

    /**
     * 全部-听单更多点击事件
     */
    fun clickSheetMoreFun() {
        curType.postValue(REQUEST_TYPE_SHEET)
    }

    /**
     * 全部-书籍点击事件
     */
    fun clickBookFun(view: View,bean:AudioBean) {
        RouterHelper.createRouter(HomeService::class.java).toDetailActivity(view.context,bean.audio_id)
    }

    /**
     * 全部-主播点击事件
     */
    fun clickAnchorFun() {

    }

    /**
     * 全部-听单点击事件
     */
    fun clickSheetFun(view: View,bean:SearchSheetBean) {
        getActivity(view.context)?.let {
            RouterHelper.createRouter(HomeService::class.java).startHomeSheetDetailActivity(it,bean.sheet_id,0)
        }
    }


}