package com.rm.module_play

import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.baselisten.util.getFloattMMKV
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.SAVA_SPEED
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.utils.CacheUtils
import org.koin.core.context.loadKoinModules

/**
 * desc   : Play 组件 application 需要处理的逻辑在这里
 * date   : 2020/08/14
 * version: 1.0
 */
class PlayApplicationDelegate : IApplicationDelegate {
    override fun onCreate() {
        DLog.d(TAG, "Module Play onCreate()!!!")
        loadKoinModules(playModules)
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