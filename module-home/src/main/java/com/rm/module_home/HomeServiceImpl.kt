package com.rm.module_home

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_home.activity.menu.MenuActivity

/**
 * desc   : Home module 路由实现接口
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = ARouterModuleServicePath.PATH_HOME_SERVICE)
class HomeServiceImpl : HomeService {
    override fun startMenuActivity(context: Context) {
        MenuActivity.startActivity(context)
    }

    override fun getHomeFragment(): Fragment {
        return Fragment()
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return HomeApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }
}