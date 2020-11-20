package com.rm.business_lib.wedgit.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.util.DLog

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
class LeftAutoScrollRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var isSlidingToLeft = false
    var firstScrollLast = false
    var isShow = false
    var blockName = ""
    var lastOpenTime = System.currentTimeMillis()

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        DLog.d("suolong","blockName = $blockName onDetachedFromWindow")
        isShow = false
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isShow = true
        DLog.d("suolong","blockName = $blockName onAttachedToWindow")
        val manager = layoutManager as LinearLayoutManager
        val lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
        val itemCount = manager.itemCount
        firstScrollLast = (lastItemPosition == itemCount-1)
    }
}