package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import com.rm.baselisten.model.BaseSwipeModel
import com.rm.baselisten.view.SwipeLayout

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
@BindingAdapter("bindSwipeModel")
fun SwipeLayout.bindSwipeModel(action : ((BaseSwipeModel)->Unit)?){
    if(action == null){
        return
    }else{

    }
}
@BindingAdapter("bindSwipeOpen")
fun SwipeLayout.bindSwipeOpen(open: Boolean?){
    if(open != null && open){
        close()
    }
}