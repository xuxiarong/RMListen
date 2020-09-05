package com.rm.component_comm.listen

import androidx.fragment.app.Fragment
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : Listen module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface ListenService : ApplicationProvider {

    fun getListenFragment():Fragment
}