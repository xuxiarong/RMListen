package com.rm.module_main

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Main 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class MainApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG,"Module Main onCreate()!!!")
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Main onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Main onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Main onTrimMemory(),---level--->>>$level")
    }
}