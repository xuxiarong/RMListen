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
    val historyList = ObservableField<MutableList<String>>()

    //当前输入框的内容
    var keyWord = ObservableField<String>("")

    //当前输入框 hint
    var hintKeyword = ""

    //输入框内容监听
    var inputKeyWord: (String) -> Unit = { inputContentChangeListener(it) }

    //输入法显示/隐藏监听
    var keyboardVisibility: (Boolean, Int) -> Unit = { it, _ -> keyboardVisibilityListener(it) }

    //输入法是否显示
    val keyboardIsVisibility = ObservableField<Boolean>(false)

    //输入法搜索按钮监听
    val bindActionListener: (View) -> Unit = { clickSearchFun(it) }

    //推荐搜索数据
    private val recommendData = ObservableField<String>()

    //联想是否显示
    val suggestIsVisible = ObservableField<Boolean>(false)

    //历史是否显示
    val historyIsVisible = ObservableField<Boolean>(false)

    //推荐内容是否显示
    val recommendVisible = ObservableField<Boolean>(false)

    //清除按钮是否显示
    val clearVisible = ObservableField<Boolean>(false)

    val lastHint = ObservableField<String>("")

    //搜索是否结束
    private var resultIsEnd = true

    /**
     * 历史 item 点击事件
     */
    val historyItemClickFun: (View, String) -> Unit = { view, content ->
        SearchResultActivity.startActivity(view.context, content)
    }

    /**
     * 输入框内容改变
     */
    private fun inputContentChangeListener(content: String) {
        keyWord.set(content.trimEnd().trim())
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
        keyboardIsVisibility.set(keyboardVisibility)
        if (keyboardVisibility) {
            if (keyWord.get()!!.isEmpty()) {
                historyIsVisible.set(HISTORY_KEY.getListString().size > 0)
            } else {
                searchSuggest(keyWord.get()!!)
            }
            recommendVisible.set(false)
        } else {
            if (suggestIsVisible.get() != true) {
                recommendVisible.set(true)
                historyIsVisible.set(HISTORY_KEY.getListString().size > 0)
            }
        }
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
        var keyword =
            if (keyWord.get()!!.isEmpty()) {
                hintKeyword
            } else {
                keyWord.get()!!
            }

        if (keyword.isEmpty()) {
            keyword = lastHint.get()!!
        }
        view?.context?.let {
            SearchResultActivity.startActivity(it, keyword)
        }
    }

    /**
     * 清除历史点击事件 清除
     */
    fun clickClearHistory() {
        clearVisible.set(!clearVisible.get()!!)
    }

    /**
     * 删除历史点击事件
     */
    fun clickDeleteHistory() {
        keyWord.set("")
        hintKeyword = ""
        clearVisible.set(false)
        HISTORY_KEY.putMMKV(mutableListOf())
        historyList.set(null)
        historyIsVisible.set(false)
    }

    /**
     * 联想 item 点击事件
     */
    fun inputItemClickFun(context: Context, content: String) {
        toSearch(context, content)
    }

    /**
     * 清除输入内容
     */
    fun clickClearInput() {
        keyWord.set("")
        inputAdapter.setList(null)
        suggestIsVisible.set(false)
        recommendVisible.set(false)
    }

    /**
     * 热搜推荐item  点击事件
     */
    fun clickRecommendFun(context: Context, recommend: String) {
        toSearch(context, recommend)
    }

    /**
     * 跳转到搜索
     */
    private fun toSearch(context: Context, content: String) {
        SearchResultActivity.startActivity(context, content)
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
                onError = { it, _ ->
                    DLog.i("-------->", "searchHotRecommend:$it")
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
                    hintBannerList.set(it.keywords?.split(","))
                },
                onError = { it, _ ->
                    DLog.i("-------->", "searchHintBanner:$it")
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
                    val split = it.keywords?.split(",")
                    recommendData.set(it.keywords)
                    recommendVisible.set(true)
                    adapter.setList(split)
                },
                onError = { it, _ ->
                    DLog.i("-------->", "searchRecommend:$it")
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
                    if (it.keywords != null && it.keywords.isNotEmpty()) {
                        val list = it.keywords.split(",")
                        inputAdapter.setList(list)
                    } else {
                        inputAdapter.setList(null)
                    }
                },
                onError = { it, _ ->
                    resultIsEnd = true
                }
            )
        }
    }
}