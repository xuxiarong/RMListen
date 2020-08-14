package com.rm.listen

import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.Cxt
import com.rm.component_comm.initARouter
import com.rm.module_mine.mineModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        initARouter(this)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ListenApplication)
            modules(appModule)
            modules(mineModule)
        }
        Cxt.context=CONTEXT
    }

}