package com.rm.module_home

import com.rm.baselisten.util.LogcatUtil
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Home 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class HomeApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        LogcatUtil.i("llj","Module Home onCreate()!!!")
    }

    override fun onTerminate() {
        LogcatUtil.i("llj","Module Home onTerminate()!!!")
    }

    override fun onLowMemory() {
        LogcatUtil.i("llj","Module Home onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        LogcatUtil.i("llj","Module Home onTrimMemory(),---level--->>>$level")
    }
}