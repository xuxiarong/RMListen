package com.rm.business_lib.binding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/10/14
 * version: 1.0
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("bindPlayCount")
fun TextView.bindPlayCount(count: Int) {
    text = when {
        count < 10000 -> {
            count.toString()
        }
        count > 99999999 -> {
            "9999w+"
        }
        else -> {
            val wan = count / 10000
            val qian = count % 10000 / 1000
            val bai = count % 1000 / 100
            "$wan" + "." + "$qian" + "w"
        }
    }
}

@BindingAdapter("bindPlayCount", "bindPlayCountUnit", requireAll = false)
fun TextView.bindPlayCount(count: Int, unit: String?) {
    val charSequence: CharSequence = when {
        count < 10000 -> {
            count.toString()
        }
        count > 99999999 -> {
            "9999w+"
        }
        else -> {
            val wan = count / 10000
            val qian = count % 10000 / 1000
            val bai = count % 1000 / 100
            "$wan" + "." + "$qian" + "w"
        }
    }
    val bf = StringBuffer(charSequence)
    if (unit != null) {
        bf.append(unit)
    }
    text = bf
}

@BindingAdapter("bindPlayCountString")
fun TextView.bindPlayCountString(count: String?) {
    try {
        if (count != null) {
            bindPlayCount(count.toInt(), null)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}