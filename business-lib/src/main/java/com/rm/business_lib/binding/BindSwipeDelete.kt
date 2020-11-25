package com.rm.business_lib.binding

import androidx.databinding.BindingAdapter
import com.rm.business_lib.wedgit.swipe.SwipeDeleteLayout

/**
 * desc   :
 * date   : 2020/11/25
 * version: 1.0
 */

@BindingAdapter("bindSwipeDelete")
fun SwipeDeleteLayout.bindSwipeDelete(action : (()->Unit)?){
    showLottie(action)
}