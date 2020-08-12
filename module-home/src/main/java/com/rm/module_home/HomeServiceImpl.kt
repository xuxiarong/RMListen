package com.rm.module_home

import androidx.fragment.app.Fragment
import com.rm.component_comm.home.HomeService

/**
 * desc   :
 * date   : 2020/08/12
 * version: 1.1
 */
class HomeServiceImpl:HomeService {
    override fun getHomeFragment(): Fragment {
        return Fragment()
    }
}