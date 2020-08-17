package com.rm.component_comm

import com.rm.baselisten.BaseApplication
import com.rm.component_comm.base.ApplicationDelegate
import com.rm.component_comm.base.ApplicationManager

/**
 * desc   :
 * date   : 2020/08/14
 * version: 1.0
 */
open class ComponentApplication : BaseApplication() {
    private lateinit var applicationManager: ApplicationManager
    override fun onCreate() {
        super.onCreate()
        initARouter(this)
        initApplications()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        this.applicationManager.handleEvent(ApplicationDelegate.ApplicationEvent.trimMemory,level)
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
        applicationManager = ApplicationDelegate.with(this)?.handleEvent(ApplicationDelegate.ApplicationEvent.create)!!
    }

}