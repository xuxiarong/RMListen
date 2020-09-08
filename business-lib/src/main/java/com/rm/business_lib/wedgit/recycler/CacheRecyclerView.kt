package com.rm.business_lib.wedgit.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
class CacheRecyclerView (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RecyclerView(context, attrs, defStyleAttr) {

    init {
        val pool = RecycledViewPool()
        pool.setMaxRecycledViews(0, 20) //TYPE为0，缓存数量为0
        setRecycledViewPool(pool) //给Recycleview设置缓存池对象

    }
}