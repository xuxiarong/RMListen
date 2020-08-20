package com.rm.module_main.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.module_home.fragment.HomePagerTestFragment

class HomeTestViewPagerAdapter(fm: FragmentManager, private val size: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
       return HomePagerTestFragment()
           .apply { arguments= Bundle().apply { putString("title",position.toString()) } }
    }

    override fun getCount(): Int {
        return size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "精品$position"
    }
}