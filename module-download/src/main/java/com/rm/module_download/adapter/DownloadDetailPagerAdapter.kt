package com.rm.module_download.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

@Suppress("DEPRECATION")
class DownloadDetailPagerAdapter(
    fm: FragmentManager,
    private val tabList: MutableList<String>,
    private val fragmentList: MutableList<Fragment>
) :  FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return tabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position]
    }

}