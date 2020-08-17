package com.rm.listen

//import com.rm.business_lib.db.DaoManager.Companion.daoManager
import com.rm.component_comm.ComponentApplication
import org.koin.core.context.loadKoinModules

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenApplication : ComponentApplication() {
    override fun onCreate() {
        super.onCreate()
        loadKoinModules(appModule)
//        daoManager.initDaoManger()
    }

}