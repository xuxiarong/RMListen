package com.rm.module_main

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.main.MainService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_main.activity.MainMainActivity

/**
 * desc   : main module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_MAIN_SERVICE)
class MainServiceImpl : MainService {
    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return MainApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }

    override fun startMainActivity(context: Context,selectTab : Int) {
        MainMainActivity.startMainActivity(context = context,selectTab = selectTab)
    }
}