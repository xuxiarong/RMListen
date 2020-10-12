package com.rm.module_search.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rm.module_search.fragment.SearchRecommendFragment

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchMainAdapter(fragment: Fragment, private val list: MutableList<String>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }


    override fun createFragment(position: Int): Fragment {
        return SearchRecommendFragment.newInstance(list[position])
    }
}