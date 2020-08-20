package com.rm.module_home

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Home 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class HomeApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG,"Module Home onCreate()!!!")
        loadKoinModules(homeModule)
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Home onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Home onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Home onTrimMemory(),---level--->>>$level")
    }
}