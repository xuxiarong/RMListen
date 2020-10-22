package com.rm.business_lib.binding

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/10/22
 * version: 1.0
 */
@BindingAdapter("bindAudioTag")
fun TextView.bindAudioTag(tag : String){
    if (TextUtils.isEmpty(tag)){
        visibility = View.GONE
    }else{
        visibility = View.VISIBLE
    }
}
