package com.rm.baselisten.binding

import androidx.databinding.BindingAdapter
import com.rm.baselisten.view.progressbar.CircularProgressView

/**
 * desc   :
 * date   : 2020/11/05
 * version: 1.0
 */

@BindingAdapter("bindPlayProgress")
fun CircularProgressView.bindPlayProgress(progress : Int = 0){
    setProgress(progress)
}