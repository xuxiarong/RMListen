package com.rm.component_comm.search

import androidx.fragment.app.Fragment
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : search module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface SearchService : ApplicationProvider {

    fun getSearchFragment(): Fragment
}