package com.rm.music_exoplayer_lib

import com.rm.baselisten.util.DLog
import com.rm.component_comm.base.IApplicationDelegate
import org.koin.core.context.loadKoinModules

/**
 * desc   : Play 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class MusicApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG, "Module Play onCreate()!!!")
        loadKoinModules(musicModules)
    }

    override fun onTerminate() {
        DLog.d(TAG, "Module Play onTerminate()!!!")
    }

    override fun onLowMemory() {
        DLog.d(TAG, "Module Play onLowMemory()!!!")
    }

    override fun onTrimMemory(level: Int) {
        DLog.d(TAG, "Module Play onTrimMemory(),---level--->>>$level")
    }
}