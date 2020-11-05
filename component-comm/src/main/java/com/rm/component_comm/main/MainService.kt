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
     */
    fun startMainActivity(context: Context,selectTab : Int = 0)
}