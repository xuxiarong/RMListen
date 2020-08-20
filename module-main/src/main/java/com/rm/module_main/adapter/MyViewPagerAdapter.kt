package com.rm.module_main.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rm.module_main.TestFragment

class MyViewPagerAdapter(fm: FragmentManager, private val size: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
       return TestFragment()
           .apply { arguments= Bundle().apply { putString("title",position.toString()) } }
    }

    override fun getCount(): Int {
        return size
    }

}