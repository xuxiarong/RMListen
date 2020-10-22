package com.rm.module_mine.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.baselisten.util.DLog

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
class MineMemberPageAdapter(private val fragments: MutableList<MemberPageData>, fm: FragmentManager) :
    FragmentPagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getItem(position: Int): Fragment {
        return fragments[position].fragment
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title = fragments[position].title
        DLog.i("------>", "title:$title")
        return title
    }

    data class MemberPageData(val fragment: Fragment, val title: String)
}