package com.rm.module_search.viewmodel

import android.content.Context
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.activity.SearchResultActivity
import com.rm.module_search.adapter.SearchRecommendAdapter
import com.rm.module_search.bean.SearchHotDetailBean

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
    fun itemClickFun(context: Context, bean: SearchHotDetailBean) {
        getActivity(context)?.let {
            SearchResultActivity.startActivity(it, bean.keyword, "")
        }
    }
}