package com.rm.module_search

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.search.SearchService

/**
 * desc   : main module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ConstantsARouter.Search.PATH_SEARCH_SERVICE)
class SearchServiceImpl : SearchService {
    override fun init(context: Context?) {
    }
}