package com.rm.component_comm

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.main.MainService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.search.SearchService


/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.0
 */
class ARouterUtils {
    fun arouterTo(path: String) {
        ARouter.getInstance().build(ARouterPathConstance.PATH_MAIN_SERVICE).navigation()
    }

    companion object {
        /**
         * 获取主工程module路由服务
         * @return MainService
         */
        fun getMainService(): MainService {
            return ARouter.getInstance().build(ARouterPathConstance.PATH_MAIN_SERVICE)
                .navigation() as MainService
        }

        /**
         * 获取主页module路由服务
         * @return HomeService
         */
        fun getHomeService(): HomeService {
            return ARouter.getInstance().build(ARouterPathConstance.PATH_HOME_SERVICE)
                .navigation() as HomeService
        }

        /**
         * 获取我听module路由服务
         * @return ListenService
         */
        fun getListenService(): ListenService {
            return ARouter.getInstance().build(ARouterPathConstance.PATH_LISTEN_SERVICE)
                .navigation() as ListenService
        }

        /**
         * 获取我的module路由服务
         * @return ListenService
         */
        fun getMineService(): MineService {
            return ARouter.getInstance().build(ARouterPathConstance.PATH_MINE_SERVICE)
                .navigation() as MineService
        }

        /**
         * 获取播放module路由服务
         * @return ListenService
         */
        fun getPlayService(): PlayService {
            return ARouter.getInstance().build(ARouterPathConstance.PATH_PLAY_SERVICE)
                .navigation() as PlayService
        }

        /**
         * 获取搜索module路由服务
         * @return ListenService
         */
        fun getSearchService(): SearchService {
            return ARouter.getInstance().build(ARouterPathConstance.PATH_SEARCH_SERVICE)
                .navigation() as SearchService
        }

        fun init(application: Application) {
            if (BuildConfig.DEBUG) {
                ARouter.openLog()
                ARouter.openDebug()
            }
            ARouter.init(application)
        }
    }
}