package com.rm.module_listen

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Listen 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class ListenApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG,"Module Listen onCreate()!!!")
        loadKoinModules(listenModules)
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Listen onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Listen onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Listen onTrimMemory(),---level--->>>$level")
    }
}