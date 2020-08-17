package com.rm.component_comm.home

import android.content.Context
import androidx.fragment.app.Fragment
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : Home工程module路由服务接口
 * date   : 2020/08/12
 * version: 1.0
 */
interface HomeService : ApplicationProvider {
    fun routerTest(context: Context)
    // 获取主页Fragment
    fun getHomeFragment(): Fragment
}