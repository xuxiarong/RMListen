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
import com.rm.module_search.activity.SearchResultActivity
import com.rm.module_search.bean.*
import com.rm.module_search.repository.SearchRepository

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchMainViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    //tab 数据
    val mTabDataList = ObservableField<MutableList<String>>()

    //搜索框轮播
    val hintBannerList = ObservableField<List<String>>()

    //推荐搜索 adapter
    val adapter by lazy {
        CommonBindVMAdapter<String>(
            this,
            mutableListOf(),
            R.layout.search_adapter_recommend,
            BR.viewModel,
            BR.recommend
        )
    }

    //输入框联想 adapter
    val inputAdapter by lazy {
        CommonBindVMAdapter<String>(
            this,
            mutableListOf(),
            R.layout.search_adapter_suggest,
            BR.viewModel,
            BR.inputItem
        )
    }

    //搜索历史 adapter
    val historyAdapter by lazy {
        CommonBindVMAdapter<String>(
            this,
            mutableListOf(),
            R.layout.search_adapter_history,
            BR.viewModel,
            BR.historyItem
        ).apply {
            val list = HISTORY_KEY.getListString()
            historyIsVisible.set(list.size > 0)
            setList(list)
        }
    }

    //当前输入框的内容
    var keyWord = ObservableField<String>("")

    //当前输入框 hint
    var hintKeyword = ""

    //输入框内容监听
    var inputKeyWord: (String) -> Unit = { inputContentChangeListener(it) }

    //输入法显示/隐藏监听
    var keyboardVisibility: (Boolean) -> Unit = { keyboardVisibilityListener(it) }

    val inputText = ObservableField<String>()

    //输入法搜索按钮监听
    val bindActionListener: (View) -> Unit = { clickSearchFun(it) }

    //推荐搜索数据
    private val recommendData = ObservableField<String>()

    //联想是否显示
    val suggestIsVisible = ObservableField<Boolean>(false)

    //历史是否显示
    val historyIsVisible = ObservableField<Boolean>(false)

    //推荐内容是否现实
    val recommendVisible = ObservableField<Boolean>(true)

    var clearInput: () -> Unit = {}

    //搜索是否结束
    private var resultIsEnd = true

    /**
     * 输入框内容改变
     */
    private fun inputContentChangeListener(content: String) {
        keyWord.set(content)
        suggestIsVisible.set(true)
        //如果请求没有结束则不会去搜索
        if (resultIsEnd && content.isNotEmpty()) {
            searchSuggest(content)
        } else {
            inputAdapter.setList(null)
            historyIsVisible.set(HISTORY_KEY.getListString().size > 0)
            suggestIsVisible.set(false)
        }
    }

    /**
     * 键盘的显示隐藏监听
     */
    private fun keyboardVisibilityListener(keyboardVisibility: Boolean) {
        if (keyboardVisibility) {
            recommendVisible.set(false)
        } else {
            recommendVisible.set(true)
        }
    }

    /**
     * 搜索点击事件
     */
    fun clickSearchFun() {
        searchKeyword.set(keyWord)
        searchResult(keyWord)
    }

    /**
     * 热搜推荐
     */
    fun searchHotRecommend() {
        launchOnIO {
            repository.searchHotRecommend().checkResult(
                onSuccess = {
                    val list = mutableListOf<SearchHotRecommendBean>()
                    val tabList = mutableListOf<String>()
                    it.forEach { bean ->
                        //如果集合中存在空的tab数据则进行移除
                        if (bean.list.size > 0) {
                            list.add(bean)
                            tabList.add(bean.cate_name)
                        }
                    }
                    mTabDataList.set(tabList)
                    hotRecommend.set(list)
                },
                onError = {

                }
            )
        }
    }

    /**
     * 搜索栏轮播
     */
    fun searchHintBanner() {
        launchOnIO {
            repository.searchHintBanner().checkResult(
                onSuccess = {
                    hintBannerList.set(it.keywords.split(","))
                },
                onError = {

                }
            )
        }
    }

    /**
     * 推荐搜索
     */
    fun searchRecommend() {
        launchOnIO {
            repository.searchRecommend().checkResult(
                onSuccess = {
                    val split = it.keywords.split(",")
                    recommendData.set(it.keywords)
                    adapter.setList(split)
                },
                onError = {})
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
}