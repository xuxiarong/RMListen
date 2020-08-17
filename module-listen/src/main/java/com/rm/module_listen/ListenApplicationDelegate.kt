package com.rm.module_listen

import android.util.Log
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Listen 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class ListenApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        Log.i("llj","Module Listen onCreate()!!!")
    }

    override fun onTerminate() {
        Log.i("llj","Module Listen onTerminate()!!!")
    }

    override fun onLowMemory() {
        Log.i("llj","Module Listen onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        Log.i("llj","Module Listen onTrimMemory(),---level--->>>$level")
    }
}