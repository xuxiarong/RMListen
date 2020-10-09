package com.rm.component_comm.router

import com.alibaba.android.arouter.launcher.ARouter

/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
open class RouterHelper {
    companion object{
        /**
         * 定义一个通用，创建组件路由的方法
         * @param mClass
         * @param <T>
         * @return
        </T> */
        fun <T : ApplicationProvider?> createRouter(mClass: Class<T>?): T {
            return ARouter.getInstance().navigation(mClass)
        }

        fun <T : ApplicationProvider?> createRouter(path: String?): T {
            return ARouter.getInstance().build(path).navigation() as T
        }
    }
}