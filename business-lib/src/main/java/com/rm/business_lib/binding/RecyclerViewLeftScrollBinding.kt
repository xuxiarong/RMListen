package com.rm.business_lib.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
@BindingAdapter("bindLeftScroll")
fun RecyclerView.bindLeftScroll(action: (() -> Unit)?) {

    var isSlidingToLeft = false
    var lastOpenTime = System.currentTimeMillis()
    if (action == null) {
        return
    }
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val manager = recyclerView.layoutManager as LinearLayoutManager?
                // 获取最后一个完全显示的itemPosition
                val lastItemPosition = manager!!.findLastCompletelyVisibleItemPosition()
                val itemCount = manager.itemCount
                if(lastItemPosition<3){
                    return
                }
                // 判断是否滑动到了最后一个Item，并且是向左滑动
                if (lastItemPosition == itemCount - 1 && isSlidingToLeft) {
                    // 加载更多
                    //修复连续滑动打开执行多次回调的bug
                    if(System.currentTimeMillis() - lastOpenTime < 500){
                        lastOpenTime = System.currentTimeMillis()
                        return
                    }
                    lastOpenTime = System.currentTimeMillis()
                    action()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isSlidingToLeft = dx>10
        }
    })
}
