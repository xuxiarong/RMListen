package com.rm.module_main

import android.util.Log
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Main 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class MainApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        Log.i("llj","Module Main onCreate()!!!")
    }

    override fun onTerminate() {
        Log.i("llj","Module Main onTerminate()!!!")
    }

    override fun onLowMemory() {
        Log.i("llj","Module Main onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        Log.i("llj","Module Main onTrimMemory(),---level--->>>$level")
    }
}