package com.rm.module_home.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.module_home.fragment.HomeTopListContentFragment

class HomeTopListPagerAdapter(
    @NonNull fm: FragmentManager,
    data: MutableList<Int>
) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val mData: MutableList<Int> by lazy {
        data
    }

    override fun getItem(position: Int): Fragment {
        val type = mData[position]
        return HomeTopListContentFragment.newInstance(type)
    }

    override fun getCount(): Int {
        return mData.size
    }
}