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


//    fun setLeftScrollListen(action : ()->Unit = {}){
//        addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                // 当不滑动时
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    val manager = recyclerView.layoutManager as LinearLayoutManager?
//                    // 获取最后一个完全显示的itemPosition
//                    val lastItemPosition = manager!!.findLastCompletelyVisibleItemPosition()
//                    val itemCount = manager.itemCount
//                    // 判断是否滑动到了最后一个Item，并且是向左滑动
//                    if(firstScrollLast){
//                        action()
//                    }
//                    firstScrollLast = lastItemPosition == itemCount-1
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                isSlidingToLeft = dx>10
//                firstScrollLast = false
//            }
//        })
//    }


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