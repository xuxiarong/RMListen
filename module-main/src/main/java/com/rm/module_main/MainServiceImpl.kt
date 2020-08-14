package com.rm.module_main

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.main.MainService

/**
 * desc   : main module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ConstantsARouter.Main.PATH_MAIN_SERVICE)
class MainServiceImpl : MainService {
    override fun init(context: Context?) {
    }
}