package com.rm.module_search

import androidx.databinding.ObservableField
import com.rm.module_search.bean.SearchHotRecommendBean
import com.rm.module_search.bean.SearchResultBean

/**
 *
 * @author yuan fang
 * @date 9/28/20
 * @description
 *
 */
//全局推荐搜索数据
val hotRecommend = ObservableField<MutableList<SearchHotRecommendBean>>()

//全局搜索数据
val searchResultData = ObservableField<SearchResultBean>()

val type = ObservableField<Int>()

//当前进行搜索的关键字
val searchKeyword = ObservableField<String>("")
