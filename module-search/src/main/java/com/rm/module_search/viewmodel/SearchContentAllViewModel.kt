package com.rm.module_search.viewmodel

import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.bean.MemberBean
import com.rm.module_search.bean.SearchSheetBean
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
    val data = searchResultData

    //书籍adapter
    val bookAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_books,
            BR.viewModel,
            BR.item
        ).apply {
            searchResultData.get()?.audio_list?.let {
                if (it.size > 3) {
                    setList(it.subList(0, 3))
                } else {
                    setList(it)
                }
            }
        }
    }

    //主播adapter
    val anchorAdapter by lazy {
        CommonBindVMAdapter<MemberBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_anchor,
            BR.viewModel,
            BR.item
        ).apply {
            searchResultData.get()?.member_list?.let {

                if (it.size > 3) {
                    setList(it.subList(0, 3))
                } else {
                    setList(it)
                }
            }
        }
    }

    //听单adapter
    val sheetAdapter by lazy {
        CommonBindVMAdapter<SearchSheetBean>(
            this,
            mutableListOf(),
            R.layout.search_adapter_content_all_sheet,
            BR.viewModel,
            BR.item
        ).apply {
            searchResultData.get()?.sheet_list?.let {

                if (it.size > 3) {
                    setList(it.subList(0, 3))
                } else {
                    setList(it)
                }
            }
        }
    }


    /**
     * 全部-书籍更多点击时间
     */
    fun clickBookFun() {

    }

    /**
     * 全部-主播更多点击时间
     */
    fun clickAnchorFun() {

    }

    /**
     * 全部-听单更多点击时间
     */
    fun clickSheetFun() {

    }


}