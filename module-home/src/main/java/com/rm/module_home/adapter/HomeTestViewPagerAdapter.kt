package com.rm.module_main.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.fragment.HomePagerTestFragment

class HomeTestViewPagerAdapter(fm: FragmentManager, private val tabList: List<CategoryTabBean>) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
       return HomePagerTestFragment()
           .apply { arguments= Bundle().apply { putString("title",position.toString()) } }
    }

    override fun getCount(): Int {
        return tabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position].name
    }
}