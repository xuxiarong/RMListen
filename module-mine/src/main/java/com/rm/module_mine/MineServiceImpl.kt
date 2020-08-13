package com.rm.module_mine

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ARouterPathConstance
import com.rm.component_comm.mine.MineService

/**
 * desc   : mine module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.1
 */
@Route(path = ARouterPathConstance.PATH_MINE_SERVICE)
class MineServiceImpl : MineService {
    override fun init(context: Context?) {
    }
}