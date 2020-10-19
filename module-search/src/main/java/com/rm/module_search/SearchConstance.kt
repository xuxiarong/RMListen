package com.rm.module_search

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.module_search.bean.SearchHotRecommendBean
import com.rm.module_search.bean.SearchResultBean

/**
 *
 * @author yuan fang
 * @date 9/28/20
 * @description 搜索模块通用数据
 *
 */

const val REQUEST_TYPE_ALL = "all"//全部
const val REQUEST_TYPE_MEMBER = "member"//主播
const val REQUEST_TYPE_AUDIO = "audio"//书籍
const val REQUEST_TYPE_SHEET = "sheet"//书单
const val HISTORY_KEY = "historyKey"//搜索历史键值

//全局推荐搜索数据
val hotRecommend = ObservableField<MutableList<SearchHotRecommendBean>>()

//全局搜索数据
val searchResultData = MutableLiveData<SearchResultBean>()

//当前进行搜索的关键字
val searchKeyword = ObservableField<String>("")

//当前选中的类型
val curType = MutableLiveData<String>(REQUEST_TYPE_ALL)




