package com.rm.module_mine

import com.rm.baselisten.util.LogcatUtil
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Mine 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class MineApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        LogcatUtil.i("llj","Module Mine onCreate()!!!")
        loadKoinModules(mineModule)
    }

    override fun onTerminate() {
        LogcatUtil.i("llj","Module Mine onTerminate()!!!")
    }

    override fun onLowMemory() {
        LogcatUtil.i("llj","Module Mine onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        LogcatUtil.i("llj","Module Mine onTrimMemory(),---level--->>>$level")
    }
}