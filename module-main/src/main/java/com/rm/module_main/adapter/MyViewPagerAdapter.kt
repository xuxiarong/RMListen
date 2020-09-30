package com.rm.module_main.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.baselisten.util.DLog
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.component_comm.search.SearchService
import com.rm.module_main.TestFragment

class MyViewPagerAdapter(fm: FragmentManager, private val size: Int) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return RouterHelper.createRouter(HomeService::class.java).getHomeFragment()
            }
            1 -> {
                return RouterHelper.createRouter(SearchService::class.java).getSearchFragment()
            }
            2 -> {
                return RouterHelper.createRouter(ListenService::class.java).getListenFragment()
            }
            3 -> {
                return RouterHelper.createRouter(MineService::class.java).getMineFragment()
            }
            else -> {
                return TestFragment().apply {
                    arguments = Bundle().apply { putString("title", position.toString()) }
                }
            }
        }
    }

    override fun getCount(): Int {
        return size
    }

}