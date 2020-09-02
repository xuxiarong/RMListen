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

    if (action == null) {
        return
    }
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val manager =
                recyclerView.layoutManager as LinearLayoutManager?
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 获取最后一个完全显示的itemPosition
                val lastItemPosition = manager!!.findLastCompletelyVisibleItemPosition()
                val itemCount = manager.itemCount

                // 判断是否滑动到了最后一个Item，并且是向左滑动
                if (lastItemPosition == itemCount - 1 && isSlidingToLeft) {
                    // 加载更多
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
