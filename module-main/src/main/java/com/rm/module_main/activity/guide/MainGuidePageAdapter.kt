package com.rm.module_main.activity.guide

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * desc   :
 * date   : 2021/01/15
 * version: 1.0
 */
class MainGuidePageAdapter(fm: FragmentManager,
                           private val fragmentList: MutableList<Fragment>
) :  FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}