package com.rm.module_home.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.module_home.activity.topic.HomeTopicListActivity
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleRvModel


/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
@BindingAdapter("bindLeftScroll")
fun RecyclerView.bindLeftScroll(model : HomeAudioHorDoubleRvModel) {

    var isSlidingToLeft = false
    var scrollStopCount = 0
    var lastOpenTime = System.currentTimeMillis()

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
                    if(scrollStopCount>1){
                        HomeTopicListActivity.startActivity(
                            context!!,
                            model.block.page_id,
                            model.block.block_id,
                            model.block.topic_id,
                            model.block.block_name
                        )
                    }else{
                        scrollStopCount++
                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isSlidingToLeft = dx>10
        }
    })
}
