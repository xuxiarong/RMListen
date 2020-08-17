package com.rm.module_search

import android.util.Log
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Search 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class SearchApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        Log.i("llj","Module Search onCreate()!!!")
    }

    override fun onTerminate() {
        Log.i("llj","Module Search onTerminate()!!!")
    }

    override fun onLowMemory() {
        Log.i("llj","Module Search onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        Log.i("llj","Module Search onTrimMemory(),---level--->>>$level")
    }
}