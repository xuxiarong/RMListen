package com.rm.module_pay

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.pay.PayService
import com.rm.component_comm.router.ARouterModuleServicePath

/**
 * desc   : play module 路由服务实现类
 * date   : 2020/08/13
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_PAY_SERVICE)
class PayServiceImpl : PayService {
    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return PayApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }
}