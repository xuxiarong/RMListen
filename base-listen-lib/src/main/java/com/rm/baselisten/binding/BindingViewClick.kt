package com.rm.baselisten.binding

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bindClick")
fun View.bindClick(action :(() -> Unit)?) {
    if(action!=null){
        setOnClickListener { action() }
    }
}
