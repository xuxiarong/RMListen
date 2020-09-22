package com.rm.module_download.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DownloadDetailPagerAdapter(
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