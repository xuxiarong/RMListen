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
        DLog.d(TAG,"Module Login onCreate()!!!")
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Login onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Login onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Login onTrimMemory(),---level--->>>$level")
    }
}