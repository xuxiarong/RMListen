package com.rm.module_search.viewmodel

import android.content.Context
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
    var keyboardVisibility: (Boolean) -> Unit = { keyboardVisibilityListener(it) }

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

    //搜索历史 adapter
    val historyAdapter by lazy {
        CommonBindVMAdapter<String>(
            this,
            mutableListOf(),
            R.layout.search_adapter_result_history,
            BR.viewModel,
            BR.historyItem
        ).apply {
            val list = HISTORY_KEY.getListString()
            setList(list)
        }
    }

    /**
     * 输入框内容改变
     */
    private fun inputKeyWordChange(it: String) {
        if (it.isEmpty()) {
            historyVisible.set(inputAdapter.data.size > 0)
            suggestIsVisible.set(false)
        } else {
            showSuggest(it)
        }
        keyWord.set(it)
    }

    /**
     * 键盘的显示隐藏监听
     */
    private fun keyboardVisibilityListener(keyboardVisibility: Boolean) {
        val keywordNotNull = keyWord.get()!!.isEmpty()
        //键盘是否显示
        if (keyboardVisibility) {
            //如果输入框内容为空则显示搜索历史，否则现实联想
            if (keywordNotNull) {
                showHistory()
            } else {
                //显示联想内容
                showSuggest(keyWord.get()!!)
            }
            contentIsVisible.set(false)
        } else {
            if (keywordNotNull) {
                showSuggest(keyWord.get()!!)
            } else {
                showContent()
            }
        }

    }

    /**
     * 显示搜索历史
     */
    private fun showHistory() {
        val list = HISTORY_KEY.getListString()
        val dataNotNull = list.size > 0
        //如果搜索记录不为空则显示
        historyVisible.set(dataNotNull)
        historyAdapter.setList(list)
        suggestIsVisible.set(false)
    }

    /**
     * 显示联想
     */
    private fun showSuggest(content: String) {
        suggestIsVisible.set(true)
        contentIsVisible.set(false)
        historyVisible.set(false)
        if (resultIsEnd) {
            searchSuggest(content)
        }
    }

    /**
     * 显示内容
     */
    private fun showContent() {
        contentIsVisible.set(true)
        historyVisible.set(false)
        suggestIsVisible.set(false)
    }

    /**
     * 清除输入内容
     */
    fun clickClearInput() {
        keyWord.set("")
        inputAdapter.setList(null)
        suggestIsVisible.set(false)
        historyVisible.set(false)
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
        historyAdapter.setList(null)
        historyVisible.set(false)
    }

    /**
     * 联想 item 点击事件
     */
    fun inputItemClickFun(view: View, content: String) {
        searchKeyword.set(content)
        clickSearchFun(view)
    }

    /**
     * 历史 item 点击事件
     */
    fun historyItemClickFun(view: View, content: String) {
        searchKeyword.set(content)
        clickSearchFun(view)
    }

    /**
     * 搜索结果
     * @param keyword 搜索关键字
     */
    fun searchResult(keyword: String) {
        oldKeyword = keyword
        saveHistory(keyword)

        launchOnIO {
            repository.searchResult(keyword, REQUEST_TYPE_ALL, 1, 10).checkResult(
                onSuccess = {
                    searchResultData.set(it)
                    showContent()
                },
                onError = {
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
        if (list.indexOf(keyword) != -1) {
            return
        }

        val size = list.size
        if (size == 4) {
            list.removeAt(0)
        }
        list.add(keyword)
        HISTORY_KEY.putMMKV(list)
    }

}