package com.rm.component_comm.mine

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
interface MineService : IProvider {
    fun routerLogin(context: Context)
}