package com.rm.module_search.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.*
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_ALL
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_ANCHOR
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_BOOKS
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_CONTENT_SHEET
import com.rm.module_search.adapter.SearchMainAdapter.Companion.TYPE_RECOMMEND
import com.rm.module_search.bean.*
import com.rm.module_search.fragment.*
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
    private var keyWord = ""
    var inputKeyWord: (String) -> Unit = { keyWord = it }

    //推荐搜索数据
    private val recommendData = ObservableField<String>()

    //搜索结果
    val contentData = ObservableField<SearchResultBean>()

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
    private fun searchResult(keyword: String) {
        launchOnIO {
            repository.searchResult(keyword, "all", 1, 10).checkResult(
                onSuccess = {


//                    val bean = getBean(it)

                    mTabDataList.set(getTabList(it))
                    contentData.set(it)
                    searchResultData.set(it)
                },
                onError = {

                })
        }
    }

    fun getBean(it: SearchResultBean): SearchResultBean {
        it.member_list = gettest(it)
        it.sheet_list = gest(it)
        return it
    }

    private val url =
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598073900947&di=8889a1a78863509eb671e05fd231a8df&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F10%2F20170710210234_y3Kf5.jpeg"

    private fun gettest(it: SearchResultBean): MutableList<MemberBean> {
        val list = mutableListOf<MemberBean>()
        list.add(MemberBean(url, "12", 0, 12, "124135134 ", "周杰伦"))
        list.add(MemberBean(url, "12", 0, 12, "124135134 ", "周杰伦"))
        list.add(MemberBean(url, "12", 0, 12, "124135134 ", "周杰伦"))
        list.add(MemberBean(url, "12", 0, 12, "124135134 ", "周杰伦"))
        return list
    }

    private fun gest(it: SearchResultBean): MutableList<SearchSheetBean> {
        val list = mutableListOf<SearchSheetBean>()
        list.add(
            SearchSheetBean(
                29,
                "2002/09/12",
                10,
                url,
                "124135134 ",
                "周杰伦"
            )
        )
        list.add(
            SearchSheetBean(
                29,
                "2002/09/12",
                10,
                url,
                "124135134 ",
                "周杰伦"
            )
        )
        list.add(
            SearchSheetBean(
                29,
                "2002/09/12",
                10,
                url,
                "124135134 ",
                "周杰伦"
            )
        )
        list.add(
            SearchSheetBean(
                29,
                "2002/09/12",
                10,
                url,
                "124135134 ",
                "周杰伦"
            )
        )
        return list
    }

    private fun getTabList(bean: SearchResultBean): MutableList<SearchContentBean> {
        val list = mutableListOf<SearchContentBean>()
        list.add(SearchContentBean(TYPE_CONTENT_ALL, "全部", SearchContentAllFragment()))

        if (bean.audio_list?.size ?: 0 > 0) {
            list.add(SearchContentBean(TYPE_CONTENT_BOOKS, "书籍", SearchContentBooksFragment()))
        }
        if (bean.member_list?.size ?: 0 > 0) {
            list.add(
                SearchContentBean(
                    TYPE_CONTENT_ANCHOR,
                    "主播",
                    SearchContentAnchorFragment()
                )
            )
        }
        if (bean.sheet_list?.size ?: 0 > 0) {
            list.add(SearchContentBean(TYPE_CONTENT_SHEET, "听单", SearchContentSheetFragment()))
        }

        return list
    }
}