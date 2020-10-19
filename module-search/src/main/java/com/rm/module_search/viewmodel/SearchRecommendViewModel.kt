package com.rm.module_search.viewmodel

import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.activity.SearchResultActivity
import com.rm.module_search.adapter.SearchRecommendAdapter
import com.rm.module_search.bean.SearchHotDetailBean
import com.rm.module_search.searchKeyword

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchRecommendViewModel : BaseVMViewModel() {
    val adapter by lazy { SearchRecommendAdapter(this) }

    /**
     * item点击事件
     */
    fun itemClickFun(bean: SearchHotDetailBean) {
        searchKeyword.set(bean.keyword)
        startActivity(SearchResultActivity::class.java)
    }
}