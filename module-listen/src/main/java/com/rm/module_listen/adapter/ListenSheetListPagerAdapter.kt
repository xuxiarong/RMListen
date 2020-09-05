package com.rm.module_listen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rm.module_listen.fragment.ListenSheetCollectedListFragment
import com.rm.module_listen.fragment.ListenSheetMyListFragment

class ListenSheetListPagerAdapter(
    activity: FragmentActivity,
    mutableList: MutableList<String>
) : FragmentStateAdapter(activity) {

    private val mDataList = mutableList

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (mDataList[position]) {
            "我的听单" -> {
                ListenSheetMyListFragment.newInstance()
            }
            else -> {
                ListenSheetCollectedListFragment.newInstance()
            }
        }
    }

}