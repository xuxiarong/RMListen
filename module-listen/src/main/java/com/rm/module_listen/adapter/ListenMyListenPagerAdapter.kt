package com.rm.module_listen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ListenMyListenPagerAdapter(
    activity: FragmentActivity,
    mutableList: MutableList<Fragment>
) : FragmentStateAdapter(activity) {

    private val mDataList = mutableList

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mDataList[position]
    }

}