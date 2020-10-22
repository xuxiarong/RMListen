package com.rm.module_mine.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 * @author yuanfang
 * @date 10/22/20
 * @description
 *
 */
class MineFollowAndFansAdapter(
    activity: FragmentActivity,
    private val fragments: MutableList<Fragment>
) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}