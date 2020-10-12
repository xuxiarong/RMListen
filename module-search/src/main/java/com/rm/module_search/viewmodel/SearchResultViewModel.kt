package com.rm.module_search.viewmodel

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.getListString
import com.rm.baselisten.util.mmkv
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.*
import com.rm.module_search.activity.SearchResultActivity
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ALL
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ANCHOR
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_BOOKS
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_SHEET
import com.rm.module_search.repository.SearchRepository
import com.tencent.mmkv.MMKV

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchResultViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    //当前输入框的内容
    private var keyWord = searchKeyword

    val tabList by lazy {
        mutableListOf(
            TYPE_CONTENT_ALL,
            TYPE_CONTENT_BOOKS,
            TYPE_CONTENT_ANCHOR,
            TYPE_CONTENT_SHEET
        )
    }

    //输入法搜索按钮监听
    val bindActionListener: (View) -> Unit = { clickSearchFun(it) }

    //输入框内容监听
    var inputKeyWord: (String) -> Unit = { keyWord.set(it) }

    /**
     * 取消点击事件
     */
    fun clickCancelFun() {
        finish()
    }

    /**
     * 搜索点击事件
     */
    private fun clickSearchFun(view: View?) {
        view?.let {
            val imm =
                it.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                imm.hideSoftInputFromWindow(it.applicationWindowToken, 0)
            }
        }
        keyWord.get()?.let {
            searchResult(it)
        }
    }

    /**
     * 搜索结果
     * @param keyword 搜索关键字
     */
    fun searchResult(keyword: String) {
        saveHistory(keyword)
        launchOnIO {
            repository.searchResult(keyword, REQUEST_TYPE_ALL, 1, 10).checkResult(
                onSuccess = {
                    searchResultData.set(it)
                },
                onError = {

                })
        }
    }


    private fun saveHistory(keyword: String) {
        val list = HISTORY_KEY.getListString()
        if (list.indexOf(keyword)!=-1){
            return
        }

        val size = list.size
        if (size <= 8) {
            list.add(keyword)
        } else {
            list[0] = keyword
        }
        HISTORY_KEY.putMMKV(list)
    }

}