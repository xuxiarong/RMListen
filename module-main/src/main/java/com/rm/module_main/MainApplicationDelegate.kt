package com.rm.module_main

import com.rm.baselisten.util.LogcatUtil
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Main 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class MainApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        LogcatUtil.i("llj","Module Main onCreate()!!!")
    }

    override fun onTerminate() {
        LogcatUtil.i("llj","Module Main onTerminate()!!!")
    }

    override fun onLowMemory() {
        LogcatUtil.i("llj","Module Main onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        LogcatUtil.i("llj","Module Main onTrimMemory(),---level--->>>$level")
    }
}