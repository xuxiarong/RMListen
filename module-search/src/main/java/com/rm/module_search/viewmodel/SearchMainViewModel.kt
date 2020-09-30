package com.rm.module_search.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.BR
import com.rm.module_search.R
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_ALL
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_ANCHOR
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_BOOKS
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_SHEET
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_RECOMMEND
import com.rm.module_search.bean.SearchContentBean
import com.rm.module_search.bean.SearchHotRecommendBean
import com.rm.module_search.bean.SearchResultBean
import com.rm.module_search.fragment.*
import com.rm.module_search.hotRecommend
import com.rm.module_search.repository.SearchRepository
import com.rm.module_search.searchResultData

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchMainViewModel(private val repository: SearchRepository) : BaseVMViewModel() {
    //tab 数据
    val mTabDataList = ObservableField<MutableList<SearchContentBean>>()

    val adapter by lazy {
        CommonBindVMAdapter<String>(
            this,
            mutableListOf(),
            R.layout.search_adapter_recommend,
            BR.viewModel,
            BR.recommend
        )
    }

    //推荐搜索数据
    private val recommendData = ObservableField<String>()

    //搜索结果
    val contentData = ObservableField<SearchResultBean>()

    //是否已经添加搜索结果
    val hasAddContentTab = ObservableField<Boolean>(false)

    /**
     * 热搜推荐
     */
    fun searchHotRecommend() {
        launchOnIO {
            repository.searchHotRecommend().checkResult(
                onSuccess = {
                    val list = mutableListOf<SearchHotRecommendBean>()
                    val tabList = mutableListOf<SearchContentBean>()
                    it.forEach { bean ->
                        //如果集合中存在空的tab数据则进行移除
                        if (bean.list.size > 0) {
                            list.add(bean)
                            tabList.add(
                                SearchContentBean(
                                    TYPE_RECOMMEND,
                                    bean.cate_name,
                                    SearchRecommendFragment.newInstance(bean.cate_name)
                                )
                            )
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
     * 搜索结果
     * @param keyword 搜索关键字
     */
    fun searchResult(keyword: String) {
        launchOnIO {
            repository.searchResult(keyword, "all", 1, 10).checkResult(
                onSuccess = {
                    if (hasAddContentTab.get() == false) {
                        mTabDataList.set(getTabList())
                    }
                    contentData.set(it)
                    searchResultData.set(it)
                },
                onError = {})
        }
    }

    private fun getTabList(): MutableList<SearchContentBean> {
        hasAddContentTab.set(true)
        val list = mutableListOf<SearchContentBean>()
        list.add(SearchContentBean(TYPE_CONTENT_ALL, "全部", SearchContentAllFragment()))
        list.add(SearchContentBean(TYPE_CONTENT_BOOKS, "书籍", SearchContentBooksFragment()))
        list.add(SearchContentBean(TYPE_CONTENT_ANCHOR, "主播", SearchContentAnchorFragment()))
        list.add(SearchContentBean(TYPE_CONTENT_SHEET, "听单", SearchContentSheetFragment()))
        return list
    }
}