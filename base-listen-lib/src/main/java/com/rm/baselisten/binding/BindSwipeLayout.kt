package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BaseSwipeModel
import com.rm.baselisten.view.swipe.SwipeLayout

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */

@BindingAdapter("bindSwipeModel","bindSwipeAction",requireAll = false)
fun SwipeLayout.bindSwipeModel(model: BaseSwipeModel?, action: ((BaseSwipeModel?) -> Unit)?){
    addSwipeListener(object : SwipeLayout.SwipeListener{
        override fun onOpen(layout: SwipeLayout?) {
            model?.close = false
            if(action!=null){
                action(model)
            }
        }
        override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
        }

        override fun onStartOpen(layout: SwipeLayout?) {
        }

        override fun onStartClose(layout: SwipeLayout?) {
        }

        override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
        }

        override fun onClose(layout: SwipeLayout?) {
            model?.close = true
        }
    })
}

@BindingAdapter("bindSwipeOpen")
fun SwipeLayout.bindSwipeOpen(model: BaseSwipeModel?){
    if(model!=null && !model.close){
        close()
    }
}