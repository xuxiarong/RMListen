package com.rm.module_main.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.TestFragment

class MyViewPagerAdapter(fm: FragmentManager, private val size: Int) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        if (position == 0) {
            val homeService = RouterHelper.createRouter(HomeService::class.java)
            return homeService.getHomeFragment()
        } else if (position == 2) {
            return RouterHelper.createRouter(ListenService::class.java).getListenFragment()
        }

        return TestFragment().apply {
            arguments = Bundle().apply { putString("title", position.toString()) }
        }
    }

    override fun getCount(): Int {
        return size
    }

}