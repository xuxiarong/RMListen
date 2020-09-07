package com.rm.baselisten.binding

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.rm.baselisten.R
import com.rm.baselisten.util.DLog

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bindText")
fun TextView.bindText(content: Any?) {

    visibility = if (TextUtils.isEmpty(content.toString())) {
        View.GONE
    } else {
        text = content.toString()
        View.VISIBLE
    }
}

