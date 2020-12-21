package com.rm.component_comm.main

import android.content.Context
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : Main主工程module路由服务接口
 * date   : 2020/08/13
 * version: 1.1
 */
interface MainService : ApplicationProvider {
    /**
     *  跳转到首页
     *  @param context 上下文
     *  @param selectTab 选择跳转到首页的哪个tab (0 -> 首页  1 -> 搜索 2 -> 我听 3 -> 我的)
     */
    fun startMainActivity(context: Context,selectTab : Int = 0)
}