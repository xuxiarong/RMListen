package com.rm.module_listen

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/09/10
 * version: 1.0
 */
@BindingAdapter("bindDateTextColor")
fun TextView.bindDateTextColor(select: Boolean) {
    setTextColor(
        if (select) {
            ContextCompat.getColor(context, R.color.business_text_color_333333)
        } else {
            ContextCompat.getColor(context, R.color.business_text_color_b1b1b1)
        }
    )
}