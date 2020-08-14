package com.rm.baselisten.binding

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bindText")
fun TextView.bindText(content: String?) {

    visibility = if (TextUtils.isEmpty(content)) {
        View.GONE
    } else {
        text = content
        View.VISIBLE
    }
}
