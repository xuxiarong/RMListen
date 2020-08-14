package com.rm.module_home

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ARouterPathConstance
import com.rm.component_comm.home.HomeService

/**
 * desc   : Home module 路由实现接口
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = ARouterPathConstance.PATH_HOME_SERVICE)
class HomeServiceImpl:HomeService {
    override fun routerTest(context: Context) {
        TestActivity.startActivity(context)
    }

    override fun getHomeFragment(): Fragment {
        return Fragment()
    }

    override fun init(context: Context?) {
    }
}