package com.rm.module_login

import android.util.Log
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Login 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class LoginApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        Log.i("llj","Module Login onCreate()!!!")
    }

    override fun onTerminate() {
        Log.i("llj","Module Login onTerminate()!!!")
    }

    override fun onLowMemory() {
        Log.i("llj","Module Login onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        Log.i("llj","Module Login onTrimMemory(),---level--->>>$level")
    }
}