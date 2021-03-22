package com.rm.module_home.binding

import androidx.databinding.BindingAdapter
import com.rm.module_home.widget.recycler.LeftAutoScrollRecyclerView
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleRvModel


/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
@BindingAdapter("bindLeftScroll")
fun LeftAutoScrollRecyclerView.bindLeftScroll(model: HomeAudioHorDoubleRvModel) {
    blockModel = model
}



