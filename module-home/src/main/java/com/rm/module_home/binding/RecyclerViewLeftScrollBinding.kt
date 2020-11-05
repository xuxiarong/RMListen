package com.rm.module_home.binding

import android.os.SystemClock
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.business_lib.wedgit.recycler.LeftAutoScrollRecyclerView
import com.rm.module_home.activity.topic.HomeTopicListActivity
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleRvModel


/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
@BindingAdapter("bindLeftScroll")
fun LeftAutoScrollRecyclerView.bindLeftScroll(model: HomeAudioHorDoubleRvModel) {

    blockName = model.block.block_name
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val manager = recyclerView.layoutManager as LinearLayoutManager?
                // 获取最后一个完全显示的itemPosition
                val lastItemPosition = manager!!.findLastCompletelyVisibleItemPosition()
                val itemCount = manager.itemCount
                // 判断是否滑动到了最后一个Item，并且是向左滑动
                if (firstScrollLast && lastItemPosition == itemCount - 1 && blockName == model.block.block_name) {
                    val openTime = SystemClock.currentThreadTimeMillis()
                    if(openTime - lastOpenTime <=100){
                        lastOpenTime =  openTime
                        return
                    }
                    lastOpenTime =  openTime
                    HomeTopicListActivity.startActivity(
                        context!!,
                        model.block.page_id,
                        model.block.block_id,
                        model.block.topic_id,
                        model.block.block_name
                    )
                }
                firstScrollLast = lastItemPosition == itemCount - 1
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isSlidingToLeft = dx > 10
            firstScrollLast = false
        }
    })

}
