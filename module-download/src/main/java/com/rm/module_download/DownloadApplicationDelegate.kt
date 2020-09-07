package com.rm.module_download

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Download 组件 application 需要处理的逻辑在这里
 * date   : 2020/09/03
 * version: 1.0
 */
class DownloadApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG,"Module Download onCreate()!!!")
        loadKoinModules(downloadModules)
    }

    override fun onTerminate() {
        DLog.d(TAG,"Module Download onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG,"Module Download onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG,"Module Download onTrimMemory(),---level--->>>$level")
    }
}