package com.rm.module_search.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.getListString
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.*
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ALL
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_ANCHOR
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_BOOKS
import com.rm.module_search.adapter.SearchResultAdapter.Companion.TYPE_CONTENT_SHEET
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.repository.SearchRepository

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchResultViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    //当前输入框的内容
    var keyWord = searchKeyword

    //输入法显示/隐藏监听
    var keyboardVisibility: (Boolean,Int) -> Unit = {it,_-> keyboardVisibilityListener(it) }

    //输入法是否显示
    val keyboardIsVisibility = ObservableField<Boolean>(false)

    //历史内容是否显示
    val historyVisible = ObservableField<Boolean>(false)

    //联想是否显示
    val suggestIsVisible = ObservableField<Boolean>(false)

    //内容是否显示
    val contentIsVisible = ObservableField<Boolean>(true)

    //输入法搜索按钮监听
    val bindActionListener: (View) -> Unit = { clickSearchFun(it) }

    //输入框内容监听
    var inputKeyWord: (String) -> Unit = { inputKeyWordChange(it) }

    //上一次搜索的文本
    private var oldKeyword = ""

    //搜索是否结束
    private var resultIsEnd = true

    val tabList by lazy {
        mutableListOf(
            TYPE_CONTENT_ALL,
            TYPE_CONTENT_BOOKS,
            TYPE_CONTENT_ANCHOR,
            TYPE_CONTENT_SHEET
        )
    }

    //输入框联想 adapter
    val inputAdapter by lazy {
        CommonBindVMAdapter<String>(
            this,
            mutableListOf(),
            R.layout.search_adapter_result_suggest,
            BR.viewModel,
            BR.inputItem
        )
    }

    val historyList = ObservableField<MutableList<String>>(HISTORY_KEY.getListString())

    /**
     * 输入框内容改变
     */
    private fun inputKeyWordChange(content: String) {
        keyWord.set(content.trimEnd().trim())
        suggestIsVisible.set(true)
        //如果请求没有结束则不会去搜索
        if (resultIsEnd && content.isNotEmpty()) {
            searchSuggest(content)
        } else {
            inputAdapter.setList(null)
            historyVisible.set(HISTORY_KEY.getListString().size > 0)
            suggestIsVisible.set(false)
        }

    }

    /**
     * 键盘的显示隐藏监听
     */
    private fun keyboardVisibilityListener(keyboardVisibility: Boolean) {
        keyboardIsVisibility.set(keyboardVisibility)
        //键盘是否显示
        if (keyboardVisibility) {
            contentIsVisible.set(false)
            if (keyWord.get()!!.isEmpty()) {
                historyVisible.set(HISTORY_KEY.getListString().size > 0)
            } else {
                searchSuggest(keyWord.get()!!)
                suggestIsVisible.set(true)
            }
        } else {
            contentIsVisible.set(true)
            suggestIsVisible.set(false)
            historyVisible.set(false)
        }

    }

    /**
     * 清除输入内容
     */
    fun clickClearInput() {
        keyWord.set("")
        inputAdapter.setList(null)
        suggestIsVisible.set(false)
        historyVisible.set(HISTORY_KEY.getListString().size > 0)
        contentIsVisible.set(false)
    }

    /**
     * 搜索点击事件
     */
    fun clickSearchFun(view: View?) {
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
     * 清除搜索历史
     */
    fun clickClearHistory() {
        HISTORY_KEY.putMMKV(mutableListOf())
        historyList.set(null)
        historyVisible.set(false)
    }

    /**
     * 联想 item 点击事件
     */
    fun inputItemClickFun(view: View, content: String) {
        keyWord.set(content)
        clickSearchFun(view)
    }

    /**
     * 历史 item 点击事件
     */
    val historyItemClickFun: (View, String) -> Unit = { view, content ->
        keyWord.set(content)
        clickSearchFun(view)
    }

    /**
     * 搜索结果
     * @param keyword 搜索关键字
     */
    fun searchResult(keyword: String) {
        if (keyword.trim().trimEnd().isEmpty()) {
            showTip("搜索的内容不能为空", R.color.business_color_ff5e5e)
            DLog.i("=====", Log.getStackTraceString(Throwable()))
            return
        }
        oldKeyword = keyword
        saveHistory(keyword)
        showLoading()
        launchOnIO {
            repository.searchResult(keyword, REQUEST_TYPE_ALL, 1, 12).checkResult(
                onSuccess = {
                    showContentView()
                    searchResultData.postValue(it)
                    historyVisible.set(false)
                    suggestIsVisible.set(false)
                    contentIsVisible.set(true)
                },
                onError = {
                    showContentView()
                    searchResultData.postValue(
                        SearchResultBean(
                            0,
                            mutableListOf(),
                            0,
                            mutableListOf(),
                            0,
                            mutableListOf()
                        )
                    )
                    showTip("$it")
                    DLog.i("------>", "$it")
                })
        }
    }

    /**
     * 下拉框搜索
     * @param keyword 搜索关键字
     */
    private fun searchSuggest(keyword: String) {
        resultIsEnd = false
        launchOnIO {
            repository.searchSuggest(keyword).checkResult(
                onSuccess = {
                    resultIsEnd = true
                    val list = it.keywords.split(",")
                    inputAdapter.setList(list)
                },
                onError = {
                    resultIsEnd = true
                }
            )
        }
    }

    /**
     * 保存首次历史
     */
    private fun saveHistory(keyword: String) {
        if (keyword.isEmpty()) {
            return
        }
        val list = HISTORY_KEY.getListString()
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next == keyword) {
                iterator.remove()
            }
        }
        val size = list.size
        if (size == 15) {
            list.removeAt(size - 1)
        }
        list.add(0, keyword)
        HISTORY_KEY.putMMKV(list)
    }

}