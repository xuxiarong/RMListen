package com.rm.module_mine

import android.util.Log
import com.rm.component_comm.base.IApplicationDelegate

/**
 * desc   : Mine 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class MineApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        Log.i("llj","Module Mine onCreate()!!!")
    }

    override fun onTerminate() {
        Log.i("llj","Module Mine onTerminate()!!!")
    }

    override fun onLowMemory() {
        Log.i("llj","Module Mine onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        Log.i("llj","Module Mine onTrimMemory(),---level--->>>$level")
    }
}