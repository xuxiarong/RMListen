package com.rm.module_search

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Search 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class SearchApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG,"Module Search onCreate()!!!")
        loadKoinModules(searchModules)
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Search onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Search onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Search onTrimMemory(),---level--->>>$level")
    }
}