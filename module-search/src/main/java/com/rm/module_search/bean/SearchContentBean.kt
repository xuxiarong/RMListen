package com.rm.module_search.bean

import androidx.fragment.app.Fragment
import com.rm.module_search.adapter.SearchMainAdapter

/**
 *
 * @author yuanfang
 * @date 9/29/20
 * @description
 *
 */
data class SearchContentBean(@SearchMainAdapter.FragmentType val type: Int, val title: String,val fragment: Fragment)