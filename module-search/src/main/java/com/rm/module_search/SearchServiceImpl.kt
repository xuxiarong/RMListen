package com.rm.module_search

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.component_comm.search.SearchService
import com.rm.module_search.fragment.SearchMainFragment

/**
 * desc   : main module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_SEARCH_SERVICE)
class SearchServiceImpl : SearchService {
    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return SearchApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }

    override fun getSearchFragment(): Fragment {
        return SearchMainFragment()
    }
}