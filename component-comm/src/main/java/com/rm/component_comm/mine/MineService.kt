package com.rm.component_comm.mine

import android.content.Context
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
interface MineService : ApplicationProvider {
    fun routerLogin(context: Context)
}