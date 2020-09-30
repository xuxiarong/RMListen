package com.rm.module_search.viewmodel

import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_search.adapter.SearchRecommendAdapter

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchRecommendViewModel : BaseVMViewModel() {
    val adapter by lazy { SearchRecommendAdapter(this) }
}