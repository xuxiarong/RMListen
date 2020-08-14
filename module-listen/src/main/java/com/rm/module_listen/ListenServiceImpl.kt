package com.rm.module_listen

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ConstantsARouter
import com.rm.component_comm.listen.ListenService

/**
 * desc   : listen module 路由服务实现类
 * date   : 2020/08/12
 * version: 1.1
 */
@Route(path = ConstantsARouter.Listen.PATH_LISTEN_SERVICE)
class ListenServiceImpl : ListenService {
    override fun init(context: Context?) {
    }
}