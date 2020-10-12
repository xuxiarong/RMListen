package com.rm.module_search.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rm.module_search.fragment.SearchContentAllFragment
import com.rm.module_search.fragment.SearchContentAnchorFragment
import com.rm.module_search.fragment.SearchContentBooksFragment
import com.rm.module_search.fragment.SearchContentSheetFragment

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchResultAdapter(fragment: FragmentActivity, private val list: MutableList<Int>) :
    FragmentStateAdapter(fragment) {

    companion object {
        const val TYPE_CONTENT_ALL = 2//搜索全部
        const val TYPE_CONTENT_BOOKS = 3//搜索书籍
        const val TYPE_CONTENT_ANCHOR = 4//搜索主播
        const val TYPE_CONTENT_SHEET = 5//搜索听单
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun createFragment(position: Int): Fragment {
        return when (list[position]) {
            TYPE_CONTENT_ALL -> {
                SearchContentAllFragment()
            }
            TYPE_CONTENT_BOOKS -> {
                SearchContentBooksFragment()
            }
            TYPE_CONTENT_ANCHOR -> {
                SearchContentAnchorFragment()
            }
            TYPE_CONTENT_SHEET -> {
                SearchContentSheetFragment()
            }
            else -> {
                SearchContentAllFragment()
            }
        }
    }
}