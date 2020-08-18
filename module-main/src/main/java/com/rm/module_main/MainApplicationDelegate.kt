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
        DLog.i("llj","Module Main onCreate()!!!")
    }

    override fun onTerminate() {
        DLog.i("llj","Module Main onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.i("llj","Module Main onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.i("llj","Module Main onTrimMemory(),---level--->>>$level")
    }
}