package com.rm.module_play

import com.rm.baselisten.util.LogcatUtil
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Play 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class PlayApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        LogcatUtil.i("llj","Module Play onCreate()!!!")
    }

    override fun onTerminate() {
        LogcatUtil.i("llj","Module Play onTerminate()!!!")
    }

    override fun onLowMemory() {
        LogcatUtil.i("llj","Module Play onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        LogcatUtil.i("llj","Module Play onTrimMemory(),---level--->>>$level")
    }
}