package com.rm.component_comm

import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.Cxt
import com.rm.component_comm.base.ApplicationDelegate
import com.rm.component_comm.base.ApplicationManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
open class ComponentApplication : BaseApplication() {
    private lateinit var applicationManager: ApplicationManager

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.base_activity_bg_color) //全局设置主题颜色
            ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.base_activity_bg_color) //全局设置主题颜色
            ClassicsFooter(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ComponentApplication)
        }
        Cxt.context = CONTEXT
        initARouter(this)
        initApplications()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        this.applicationManager.handleEvent(ApplicationDelegate.ApplicationEvent.trimMemory, level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        this.applicationManager.handleEvent(ApplicationDelegate.ApplicationEvent.lowMemory)
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationManager.handleEvent(ApplicationDelegate.ApplicationEvent.terminate)
    }

    private fun initApplications() {
        applicationManager = ApplicationDelegate.with(this)
            ?.handleEvent(ApplicationDelegate.ApplicationEvent.create)!!
    }

}