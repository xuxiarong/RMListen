package com.rm.listen

import com.rm.baselisten.util.Cxt
import com.rm.business_lib.db.DaoManager.Companion.daoManager
import com.rm.component_comm.ComponentApplication
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
class ListenApplication : ComponentApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ListenApplication)
            modules(appModule)
            modules(mineModule)
        }
        Cxt.context=CONTEXT
        daoManager.initDaoManger()
    }

}