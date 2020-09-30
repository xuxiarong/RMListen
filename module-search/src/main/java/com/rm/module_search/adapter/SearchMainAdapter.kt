package com.rm.module_search.adapter

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rm.module_search.bean.SearchContentBean

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchMainAdapter(
    fragment: Fragment,
    private var contentList: MutableList<SearchContentBean>
) : FragmentStateAdapter(fragment) {
    companion object {
        const val TYPE_RECOMMEND = 1//推荐搜索
        const val TYPE_CONTENT_ALL = 2//搜索全部
        const val TYPE_CONTENT_BOOKS = 3//搜索书籍
        const val TYPE_CONTENT_ANCHOR = 4//搜索主播
        const val TYPE_CONTENT_SHEET = 5//搜索听单
    }

    override fun getItemCount(): Int {
        return contentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return contentList[position].fragment ?: Fragment()
    }

    @IntDef(
        TYPE_RECOMMEND,
        TYPE_CONTENT_ALL,
        TYPE_CONTENT_BOOKS,
        TYPE_CONTENT_ANCHOR,
        TYPE_CONTENT_SHEET
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class FragmentType(val type: Int = TYPE_RECOMMEND)

}