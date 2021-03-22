package com.rm.component_comm.router

import com.alibaba.android.arouter.facade.template.IProvider
import com.rm.component_comm.base.IApplicationDelegate
import java.util.*

/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
interface ApplicationProvider : IProvider {
    /**
     * 获取每个组件application
     *
     * @return
     */
    fun getApplicationDelegateClass(): Class<out IApplicationDelegate?>

    companion object {
        /**
         * 查找每个组件的Application代理类
         *
         * @return
         */
        fun queryApplicationDelegate(): List<IApplicationDelegate> {
            val applicationDelegateList: MutableList<IApplicationDelegate> =
                ArrayList()
            val applicationClassList: List<Class<out IApplicationDelegate>> =
                queryApplicationClassList()
            for (mClass in applicationClassList) {
                try {
                    val delegate: IApplicationDelegate = mClass.getConstructor().newInstance()
                    applicationDelegateList.add(delegate)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return applicationDelegateList
        }

        private fun queryApplicationClassList(): List<Class<out IApplicationDelegate?>> {
            val applicationClassList: MutableList<Class<out IApplicationDelegate?>> =
                ArrayList()
            val routerPathList =
                queryRouterPath()
            for (routerPath in routerPathList) {
                val provider: ApplicationProvider = RouterHelper.createRouter(routerPath)
                    ?: continue
                applicationClassList.add(provider.getApplicationDelegateClass())
            }
            return applicationClassList
        }

        /**
         * 查找全部组件的router path
         * @return
         */
        private fun queryRouterPath(): List<String> {
            val list: MutableList<String> =
                ArrayList()
            try {
                val pathClass: Class<*> = ARouterModuleServicePath::class.java
                val fields = pathClass.declaredFields
                for (field in fields) {
                    field.isAccessible = true
                    val routerPath = field[null] as? String
                    list.add(routerPath?:"")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return list
        }
    }

}