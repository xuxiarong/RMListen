package com.rm.baselisten.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bindSrc")
fun ImageView.bindSrc(resourceId: Int?) {
    if(resourceId != null && resourceId > 0){
        visibility = View.VISIBLE
        setImageResource(resourceId)
    }else{
        visibility = View.GONE
    }

}