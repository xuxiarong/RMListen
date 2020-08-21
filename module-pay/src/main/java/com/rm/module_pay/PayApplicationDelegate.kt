package com.rm.module_pay

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Pay 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class PayApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG,"Module Pay onCreate()!!!")
        loadKoinModules(payModules)
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Pay onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Pay onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Pay onTrimMemory(),---level--->>>$level")
    }
}