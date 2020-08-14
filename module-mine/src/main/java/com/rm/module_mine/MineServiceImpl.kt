package com.rm.module_mine

import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.ARouterPathConstance
import com.rm.component_comm.mine.MineService
import com.rm.module_mine.login.LoginActivity

/**
 * desc   : mine module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.1
 */
@Route(path = ARouterPathConstance.PATH_MINE_SERVICE)
class MineServiceImpl : MineService {
    override fun routerLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun init(context: Context?) {
    }
}