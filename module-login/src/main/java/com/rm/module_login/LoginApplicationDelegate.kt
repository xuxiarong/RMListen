package com.rm.module_login

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Login 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class LoginApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.i("llj","Module Login onCreate()!!!")
    }

    override fun onTerminate() {
        DLog.i("llj","Module Login onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.i("llj","Module Login onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.i("llj","Module Login onTrimMemory(),---level--->>>$level")
    }
}