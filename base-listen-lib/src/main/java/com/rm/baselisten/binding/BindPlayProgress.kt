package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BasePlayProgressModel
import com.rm.baselisten.view.DragCloseLayout
import com.rm.baselisten.view.progressbar.CircularProgressView

/**
 * desc   :
 * date   : 2020/11/05
 * version: 1.0
 */

@BindingAdapter("bindPlayProgress")
fun CircularProgressView.bindPlayProgress(model : BasePlayProgressModel){
    if(model.totalDuration == 0L){
        progress = 0
    }else{
        progress = (model.currentDuration/(model.totalDuration*100)).toInt()
    }
}

@BindingAdapter("bindDragOpen")
fun DragCloseLayout.bindDragOpen(isOpen : Boolean){
    if(isOpen){
        openBottomToTop()
    }
}