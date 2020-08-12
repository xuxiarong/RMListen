package com.rm.component_comm.home

import androidx.fragment.app.Fragment
import com.rm.component_comm.IProvider

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.1
 */
interface HomeService : IProvider {
    fun getHomeFragment(): Fragment
}