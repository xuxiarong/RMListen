package com.rm.component_comm

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.rm.component_comm.home.HomeService


/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.1
 */
class ARouterUtils {
    companion object{
        fun getHomeService(): HomeService {
            return ARouter.getInstance().build(Constance.PATH_HOME_SERVICE).navigation() as HomeService
        }

        fun init(application: Application){
            if(BuildConfig.DEBUG){
                ARouter.openLog()
                ARouter.openDebug()
            }
            ARouter.init(application)
        }
    }
}